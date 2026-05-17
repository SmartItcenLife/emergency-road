package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.findpath.service.PathService;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.recommend.dto.*;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalRecommendationService {

    // 모든 추천 전략들을 주입받아서 확장 가능하게 만든 구조
    private final List<RecommendationStrategy> strategies;
    private final HospitalScoreRepository hospitalScoreRepository;
    private final PediatricCongestionCalculator pediatricCongestionCalculator;
    // 외부 API (카카오 모빌리티) 기반 거리/시간 계산 서비스
    private final PathService pathService;

    @Transactional
    public void calculateAllHospitalScores() {

        for (RecommendationStrategy strategy : strategies) {
            strategy.calculateScores();
        }
    }

    /**
     * 이동 시간 기반 가중치
     * 0분 -> 50점
     * 30분 -> 약 2점
     */
    private double calculateTimeWeight(double duration) {

        double weight = 50 - (duration * 1.6);

        return Math.max(0, weight);
    }


    /**
     * Haversine 공식 기반 직선거리 계산
     */
    private double calculateDirectDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        final int EARTH_RADIUS = 6371; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    //전체 정렬 리스트
    public List<HospitalResponseDto> getRecommendations(
            HospitalCategory category,
            Double lat,
            Double lon
    ) {

        // 1. DB에서 미리 계산된 병원 점수 가져오기
        List<HospitalScore> baseScores = getScoresByCategory(category);

        // 병원 리스트 추출
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

        List<HospitalResponseDto> result = new ArrayList<>();

        // 4. 병원별 최종 점수 계산
        for (HospitalScore score : baseScores) {
            // 기본 점수 (DB 저장된 점수)
            double baseScore = getScoreByCategory(score, category);
            // 추천 불가 병원 제외
            if (baseScore <= 0) continue;

            Hospital hospital = score.getHospital();
            // 해당 병원의 거리 정보 조회 (Map 기반 O(1))
            PathResponseDto path = pathMap.get(hospital.getHpid());

            double distance = 999;   // 기본값 (거리 정보 없을 때)
            double duration = 999;   // 기본값 (시간 정보 없을 때)
            double durationWeight = 0;

            /**
             * 카카오 모빌리티 성공
             */
            if (path != null) {

                distance = path.getDistance();
                duration = path.getDuration();

                durationWeight = calculateTimeWeight(duration);
            }

            //카카오 모빌리티 실패 fallback
            else {
                // 위도/경도 없는 병원 제외
                if (hospital.getLatitude() == null ||
                        hospital.getLongitude() == null) {
                    continue;
                }

                distance = calculateDirectDistance(
                        lat,
                        lon,
                        hospital.getLatitude(),
                        hospital.getLongitude()
                );
                // 평균 시속 40km 기준 예상 시간(분)
                duration = (distance / 40.0) * 60.0;

                durationWeight = calculateTimeWeight(duration);
            }

            // 최종 점수
            double finalScore = baseScore + (durationWeight * 2.5);

            // 일반 응급
            if (category == HospitalCategory.GENERAL) {

                result.add(
                        GeneralHospitalResponseDto.builder()
                                .hospitalName(hospital.getHospitalName())
                                .hpid(hospital.getHpid())
                                .finalScore(finalScore)
                                .distance(distance)
                                .duration(duration)
                                .address(hospital.getAddress())
                                .availableBedCount(
                                        score.getGeneralRealTimeAndStandard()
                                                .getHvec()
                                )
                                .tags(score.getGeneralTags())
                                .build()
                );
            }

            //소아 응급
            else if (category == HospitalCategory.PEDIATRIC) {

                var realtime = score.getPediatricRealtime();
                var standard = score.getPediatricStandard();

                //실시간 소아 병상 수
                Integer bed = Optional.ofNullable(realtime)
                        .map(PediatricRealtime::getPediatricBedCount)
                        .orElse(0);
                //소아 기준 병상 수
                Integer total = Optional.ofNullable(standard)
                        .map(PediatricStandard::getPediatricBedStandard)
                        .orElse(0);
                 String incubator = realtime != null ? realtime.getIncubatorResourceAvailable() : "N";

                Hospital h = score.getHospital();
                Double latitude = Optional.ofNullable(h )
                        .map(Hospital::getLatitude)
                        .orElse(0.0);

                Double longitude = Optional.ofNullable(h )
                        .map(Hospital::getLongitude)
                        .orElse(0.0);

                double percent = pediatricCongestionCalculator.getPercentage(bed, total);
                String label = pediatricCongestionCalculator.getLabel(bed, total);

                result.add(
                        PediatricHospitalResponseDto.builder()
                                .hospitalName(hospital.getHospitalName())
                                .hpid(hospital.getHpid())
                                .finalScore(finalScore)
                                .distance(distance)
                                .duration(duration)
                                .address(hospital.getAddress())
                                .availablePediatricBedCount(bed)
                                //인큐베이터 가능여부
                                .incubatorAvailable(incubator
                                )//혼잡도 라벨
                                .congestionLabel(
                                        label
                                ) //퍼센트
                                .availableBedPercentage(percent)
                                //응급실 전화번호
                                .emergencyPhone(
                                        score.getHospital().getEmergencyPhone()
                                )
                                .totalPediatricBedCount(total)
                                .hospitalLatitude(latitude)
                                .hospitalLongitude(longitude)
                                .tags(score.getPediatricTags())
                                .build()
                );
            }

            //임산부 응급
            else if (category == HospitalCategory.PREGNANT) {

                result.add(
                        PregnantHospitalResponseDto.builder()
                                .hospitalName(hospital.getHospitalName())
                                .hpid(hospital.getHpid())
                                .finalScore(finalScore)
                                .distance(distance)
                                .duration(duration)
                                .address(hospital.getAddress())
                                .tags(score.getPregnantTags())
                                // 가능 여부
                                .deliveryAvailable(
                                        score.getPregnant().getDeliveryAvailable()
                                )
                                .nicuAvailable(
                                        score.getPregnant().getNicuAvailable()
                                )
                                .obstetricSurgeryAvailable(
                                        score.getPregnant().getObstetricSurgeryAvailable()
                                )
                                .gynecologySurgeryAvailable(
                                        score.getPregnant().getGynecologySurgeryAvailable()
                                )
                                .emergencyDialysisAvailable(
                                        score.getPregnant().getEmergencyDialysisAvailable()
                                )

                                // realtime
                                .nicuBedCount(
                                        score.getPregnantRealtime().getNicuBedCount()
                                )
                                .incubatorAvailable(
                                        score.getPregnantRealtime().getIncubatorAvailable()
                                )
                                .prematureVentilatorAvailable(
                                        score.getPregnantRealtime().getPrematureVentilatorAvailable()
                                )
                                .isDeliveryRoomAvailable(
                                        score.getPregnantRealtime().getIsDeliveryRoomAvailable()
                                )

                                // standard
                                .deliveryRoomStandard(
                                        score.getPregnantStandard().getDeliveryRoomStandard()
                                )
                                .nicuStandard(
                                        score.getPregnantStandard().getNicuStandard()
                                )
                                .ventilatorStandard(
                                        score.getPregnantStandard().getVentilatorStandard()
                                )
                                .incubatorStandard(
                                        score.getPregnantStandard().getIncubatorStandard()
                                )
                                .emergencyPhone(score.getHospital().getEmergencyPhone())
                                .hospitalLatitude(score.getHospital().getLatitude())
                                .hospitalLongitude(score.getHospital().getLongitude())
                                .build()
                );
            }
        }

        // 5. 점수 기준 내림차순 정렬
        result.sort((a, b) ->
                Double.compare(
                        b.getFinalScore(),
                        a.getFinalScore()
                )
        );
        // 로그 확인용 코드
        System.out.println("====== [병원 추천 결과 리스트] ======");
        for (int i = 0; i < result.size(); i++) {
            HospitalResponseDto dto = result.get(i);
            System.out.printf("[%d위] 병원명: %s | 최종점수: %.2f | 거리: %.2fkm | 소요시간: %.1f분%n",
                    i + 1,
                    dto.getHospitalName(),
                    dto.getFinalScore(),
                    dto.getDistance(),
                    dto.getDuration());
        }
        System.out.println("==================================");
        return result;
    }

    //전체 리스트에 보여줄 애들
    public List<PediatricHospitalListDto> getPediatricHospitalList(Double lat, Double lon) {
        // 1. 기존 추천 로직 호출 (카테고리를 PEDIATRIC으로 지정)
        List<HospitalResponseDto> recommendations = getRecommendations(HospitalCategory.PEDIATRIC, lat, lon);

        // 2. PediatricHospitalResponseDto -> PediatricHospitalListDto 변환
        return recommendations.stream()
                .filter(dto -> dto instanceof PediatricHospitalResponseDto) // 소아 응급 타입만 필터링
                .map(dto -> {
                    PediatricHospitalResponseDto pDto = (PediatricHospitalResponseDto) dto;

                    return PediatricHospitalListDto.builder()
                            .hpid(pDto.getHpid())
                            .hospitalName(pDto.getHospitalName())
                            .availablePediatricBedCount(pDto.getAvailablePediatricBedCount())
                            // ResponseDto에 totalBedCount가 없다면 Score 엔티티 등에서 가져오도록 보완 필요
                            // 만약 Percentage 계산이 이미 ResponseDto에 있다면 그것을 활용
                           // .recordedAt(LocalDateTime.now()) // 현재 시간 혹은 데이터 업데이트 시간
                            //.availablePediatricBedCount(pDto.getTotalPediatricBedCount())
                            .totalPediatricBedCount(pDto.getTotalPediatricBedCount())
                            .emergencyPhone(pDto.getEmergencyPhone())
                            .hospitalLatitude(pDto.getHospitalLatitude())
                            .hospitalLongitude(pDto.getHospitalLongitude())
                            .distanceKm(pDto.getDistance())
                            .build();
                })
                .collect(Collectors.toList());
    }
    //top3만 보여줌
//    public List<HospitalResponseDto> getTop3Recommendations(
//            HospitalCategory category,
//            Double lat,
//            Double lon
//    ) {
//
//        return getRecommendations(category, lat, lon)
//                .stream()
//                .limit(3)
//                .toList();
//    }

    //소아 top3 추천
    public List<PediatricHospitalResponseDto> getTop3Pediatric(Double lat, Double lon) {
        return getRecommendations(HospitalCategory.PEDIATRIC, lat, lon)
                .stream()
                .map(r -> (PediatricHospitalResponseDto) r)
                .limit(3)
                .toList();
    }

    //TODO 일반 넘겨주기
//    public List<PediatricHospitalResponseDto> getTop3Pediatric(Double lat, Double lon) {
//        return getRecommendations(HospitalCategory.PEDIATRIC, lat, lon)
//                .stream()
//                .map(r -> (PediatricHospitalResponseDto) r)
//                .limit(3)
//                .toList();
//    }

    //TODO 임산부 넘겨주기
    public List<PregnantHospitalResponseDto> getTop3Pregnant(Double lat, Double lon) {
        return getRecommendations(HospitalCategory.PREGNANT, lat, lon)
                .stream()
                .map(r -> (PregnantHospitalResponseDto) r)
                .limit(3)
                .toList();
    }
    //카테고리별 점수 조회
    private List<HospitalScore> getScoresByCategory(
            HospitalCategory category
    ) {

        if (category == HospitalCategory.PREGNANT) {
            return hospitalScoreRepository
                    .findAllByPregnantScoreGreaterThan(0.0);
        }

        if (category == HospitalCategory.PEDIATRIC) {
            return hospitalScoreRepository
                    .findAllByPediatricScoreGreaterThan(0.0);
        }

        if (category == HospitalCategory.GENERAL) {
            return hospitalScoreRepository
                    .findAllByGeneralScoreGreaterThan(0.0);
        }

        return List.of();
    }

    //카테고리별 점수 반환
    private double getScoreByCategory(
            HospitalScore score,
            HospitalCategory category
    ) {

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