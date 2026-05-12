package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.findpath.service.PathService;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.recommend.dto.HospitalRankingResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightPregnantConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalRecommendationService {

    // 모든 추천 전략들을 주입받아서 확장 가능하게 만든 구조
    private final List<RecommendationStrategy> strategies;

    private final WeightPregnantConfigurationRepository weightPregnantConfigurationRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalScoreRepository hospitalScoreRepository;

    // 외부 API (카카오 모빌리티) 기반 거리/시간 계산 서비스
    private final PathService pathService;

    // 카테고리별 데이터 조회 (현재는 임산부만 구현됨)
    private List<?> fetchDataForCategory(HospitalCategory category) {

        if (category == HospitalCategory.PREGNANT) {
            // 임산부 추천용 projection 데이터 조회 (병원 + 점수 + 실시간 상태)
            return hospitalRepository.findAllHospitalPregnantData();

        } else if (category == HospitalCategory.PEDIATRIC) {
            // 소아 추천 데이터 (추후 구현 예정)

        } else if (category == HospitalCategory.GENERAL) {
            // 일반 응급 추천 데이터 (추후 구현 예정)
        }

        return List.of();
    }

    // 스케줄러에서 호출되는 전체 점수 갱신 로직
    // 모든 병원의 기본 점수를 미리 계산해서 DB에 저장하는 역할
    @Transactional
    public void calculateAllHospitalScores() {

        for (RecommendationStrategy strategy : strategies) {

            // strategy가 어떤 카테고리인지 확인 (임산부/소아/일반)
            if (strategy.getCategory() == null) {
                continue;
            }

            // 해당 카테고리의 가중치 설정값 조회
            WeightPregnantConfiguration config =
                    weightPregnantConfigurationRepository.findTopByCategoryOrderByCreatedAtDesc(strategy.getCategory())
                            .orElseThrow(() ->
                                    new RuntimeException(strategy.getCategory() + " 가중치 설정이 없습니다.")
                            );

            // 해당 카테고리에 맞는 병원 데이터 조회
            List<?> results = fetchDataForCategory(strategy.getCategory());

            // 실제 점수 계산 로직 실행 (전략 패턴)
            strategy.calculateScores(config, results);
        }
    }

    // 임산부 기준 점수가 0보다 큰 병원만 필터링
    // 즉, 추천 가능한 병원만 남기는 전처리 단계
    private List<HospitalScore> getPregnantScores() {
        return hospitalScoreRepository.findAllByPregnantScoreGreaterThan(0.0);
    }

    // 거리 기반 점수 계산 로직
    // 거리가 가까울수록 높은 점수를 부여하는 구조
    private double calculateDistanceWeight(double distance) {
        // 0km → 50점
        // 10km → 0점
        // 거리 1km 증가할 때마다 추천 점수를 5점씩 깎는 단순 선형 거리 패널티 모델 => 단순 로직(추후 개선 요망)
        double weight = 50 - (distance * 5);

        // 음수 방지 (최소 0점)
        return Math.max(0, weight);
    }

    // 임산부 추천 + 거리 결합 최종 TOP3 반환 로직
    public List<HospitalRankingResponseDto> getTop3Recommendations(
            HospitalCategory category, Double lat, Double lon) {

        // 1. DB에서 미리 계산된 병원 점수 가져오기
        List<HospitalScore> baseScores = getScoresByCategory(category);

        //병원 리스트 추출
        List<Hospital> hospitals = new ArrayList<>();

        for (HospitalScore score : baseScores) {
            hospitals.add(score.getHospital());
        }
        // 2. 사용자 기준 거리/시간 계산 (API 1번만 호출)
        List<PathResponseDto> paths =
                pathService.findHospitalsWithDistance(
                        new LocationRequestDto(lat, lon), hospitals);

        // 3. 병원 ID 기준 빠른 조회를 위한 Map 생성
        // O(N²) 방지 → O(1) 조회 구조로 개선
        Map<String, PathResponseDto> pathMap = new HashMap<>();

        for (PathResponseDto p : paths) {
            pathMap.put(p.getHpid(), p);
        }

        List<HospitalRankingResponseDto> result = new ArrayList<>();

        // 4. 병원별 최종 점수 계산
        for (HospitalScore score : baseScores) {

            // 기본 점수 (DB 저장된 점수)
            double baseScore = getScoreByCategory(score, category);

            // 추천 불가 병원 제외
            if (baseScore <= 0) continue;

            // 해당 병원의 거리 정보 조회 (Map 기반 O(1))
            PathResponseDto path = pathMap.get(score.getHospital().getHpid());

            double distance = 999;   // 기본값 (거리 정보 없을 때)
            double duration = 999;   // 기본값 (시간 정보 없을 때)
            double distanceWeight = 0;

            if (path != null) {

                // 실제 거리/시간 값 적용
                distance = path.getDistance();
                duration = path.getDuration();

                // 거리 점수화
                distanceWeight = calculateDistanceWeight(distance);
            }

            // 최종 점수 = 병원 기본 점수 + 거리 보정 점수
            double finalScore = baseScore + distanceWeight;

            result.add(
                    HospitalRankingResponseDto.builder()
                            .hospitalName(score.getHospital().getHospitalName())
                            .hpid(score.getHospital().getHpid())
                            .finalScore(finalScore)
                            .distance(distance)
                            .duration(duration)
                            .build()
            );
        }

        // 5. 점수 기준 내림차순 정렬
        result.sort((a, b) ->
                Double.compare(b.getFinalScore(), a.getFinalScore())
        );

        // 6. TOP 3만 반환
        if (result.size() > 3) {
            return result.subList(0, 3);
        }

        return result;
    }

    // 카테고리별 점수 선택 로직
    // 하나의 HospitalScore에서 상황에 맞는 점수만 꺼내는 구조
    private List<HospitalScore> getScoresByCategory(HospitalCategory category) {

        if (category == HospitalCategory.PREGNANT) {
            return hospitalScoreRepository.findAllByPregnantScoreGreaterThan(0.0);

        } else if (category == HospitalCategory.PEDIATRIC) {
            return List.of();

        } else if (category == HospitalCategory.GENERAL) {
            return List.of();
        }

        return List.of();
    }

    private double getScoreByCategory(HospitalScore score, HospitalCategory category) {

        if (category == HospitalCategory.PREGNANT) {
            return score.getPregnantScore();
        }

        if (category == HospitalCategory.PEDIATRIC) {
            return score.getPediatricScore();
        }

        if (category == HospitalCategory.GENERAL) {
            return score.getGeneralScore();
        }

        return 0.0;
    }
}