package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.findpath.service.TmapService;
import com.itcen.emergencyroad.findpath.service.cacaoService;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.recommend.dto.*;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.mapper.GeneralHospitalMapper;
import com.itcen.emergencyroad.recommend.mapper.PediatricHospitalMapper;
import com.itcen.emergencyroad.recommend.mapper.PregnantHospitalMapper;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalRecommendationService {

    private final List<RecommendationStrategy> strategies;
    private final HospitalScoreRepository hospitalScoreRepository;

    private final TmapService tmapService;
    private final cacaoService cacaoService;

    private final GeneralHospitalMapper generalHospitalMapper;
    private final PediatricHospitalMapper pediatricHospitalMapper;
    private final PregnantHospitalMapper pregnantHospitalMapper;

    //상수
    private static final double MAX_RADIUS_KM = 25.0;  //후보군 거리
    private static final double AVG_SPEED_KMH = 50.0; // 직선거리 계산 시 시속 km
    private static final double DURATION_WEIGHT_FACTOR = 1.6; //소요시간 가중치

    // 1. 점수 계산
    @Transactional
    public void calculateAllHospitalScores() {
        for (RecommendationStrategy strategy : strategies) {
            strategy.calculateScores();
        }
    }


    // 후보군 필터링
    private List<HospitalScore> filterCandidates(List<HospitalScore> hospitalScores, Double lat, Double lon, boolean useDistance) {
        if(!useDistance) {
            return hospitalScores;
        }
        return hospitalScores.stream()
                .filter(s -> s.getHospital().isValidLocation())
                .filter(s -> calculateDirectDistance(lat,lon, s.getHospital().getLatitude(), s.getHospital().getLongitude()) <= MAX_RADIUS_KM)
                .toList();

    }
    // 개별 병원 점수 가중치 적용 및 DTO 변환 책임
    private List<HospitalResponseDto> calculateAndMapResults(HospitalCategory category, List<HospitalScore> candidates, Map<String, PathResponseDto> routeMap, Double lat, Double lon, boolean useDistance) {
        List<HospitalResponseDto> result = new ArrayList<>();

        for (HospitalScore score : candidates) {
            double baseScore = category.getScore(score);
            if (baseScore <= 0) continue;

            double finalScore = baseScore;
            HospitalRouteInfo routeInfo = null;

            if (useDistance) {
                routeInfo = resolveRouteInfo(score.getHospital(), routeMap.get(score.getHospital().getHpid()), lat, lon);
                if (routeInfo == null) continue;
                // 시간이 오래 걸리면 점수를 깎는 방식
                // 20분이 넘어가면 점수가 깎이기 시작함
                double timePenalty = Math.max(0, (routeInfo.duration - 20) * 0.1);
                finalScore = Math.max(0, finalScore - timePenalty);
                // 10km 이내의 가까운 병원에는 보너스 점수 부여
                if (routeInfo.distance <= 10.0) {
                    finalScore += 5.0;
                }
            }

            result.add(mapToCategoryDto(category, score, finalScore, routeInfo));
        }
        return result;
    }
    // 2. 핵심 추천 로직
    public List<HospitalResponseDto> getRecommendations(
            HospitalCategory category,
            Double lat,
            Double lon,
            boolean useDistance
    ) {

        //1. 카테고리별 기본 점수 데이터 조회
        List<HospitalScore> baseScores = getScoresByCategory(category);

        //1. 후보군 필터링(반경 15km)
        List<HospitalScore> targetScores = filterCandidates(baseScores, lat, lon , useDistance);

        //2. 경로 데이터 수집(useDistance가 true일 때만)
        Map<String, PathResponseDto> routeMap = useDistance ? findDistanceAndDuration(targetScores, lat, lon) : Collections.emptyMap();

        // 3. 점수 계산 및 DTO 변환
        List<HospitalResponseDto> result = calculateAndMapResults(category, targetScores, routeMap, lat, lon, useDistance);

        // 4. 정렬 및 로깅
       result.sort((a, b) ->
               Double.compare(b.getFinalScore(), a.getFinalScore())
        );
        logRecommendationResult(result);

        return result;
    }

    // 3. Top3
    public <T extends HospitalResponseDto> List<T> getTop3(
            HospitalCategory category,
            Double lat,
            Double lon,
            Class<T> type
    ) {
        // 이미 15km로 필터링되어 계산된 추천 리스트 중 최종 거리 10km 이내' 상위 3개 추출
        return getRecommendations(category, lat, lon, true).stream()
                .filter(dto -> dto.getDistance() <= 10.0)
                .filter(type::isInstance)
                .map(type::cast)
                .limit(3)
                .toList();
    }


    // 4. 카테고리 매핑
    private HospitalResponseDto mapToCategoryDto(
            HospitalCategory category,
            HospitalScore score,
            double finalScore,
            HospitalRouteInfo routeInfo
    ) {
        double distance = routeInfo != null ? routeInfo.distance : 0;
        double duration = routeInfo != null ? routeInfo.duration : 0;

        if (category == HospitalCategory.GENERAL) {
            return generalHospitalMapper.toDto(
                    score,
                    finalScore,
                    distance,
                    duration
            );
        }

        if (category == HospitalCategory.PEDIATRIC) {
            return pediatricHospitalMapper.toDto(
                    score,
                    finalScore,
                    distance,
                    duration
            );
        }

        if (category == HospitalCategory.PREGNANT) {
            return pregnantHospitalMapper.toDto(
                    score,
                    finalScore,
                    distance,
                    duration
            );
        }

        return null;
    }


    // 5. 거리 및 소요시간
    private Map<String, PathResponseDto> findDistanceAndDuration(
            List<HospitalScore> baseScores,
            Double lat,
            Double lon
    ) {

        LocationRequestDto userLocation =
                new LocationRequestDto(lat, lon);

        List<Hospital> hospitals = new ArrayList<>();
        for (HospitalScore score : baseScores) {
            hospitals.add(score.getHospital());
        }

        List<PathResponseDto> paths = null;

        try {
            paths = cacaoService.findHospitalsWithDistance(
                    userLocation,
                    hospitals
            );
        } catch (Exception e) {
            log.error("카카오 실패: {}", e.getMessage());
        }

        if (paths == null || paths.isEmpty()) {
            try {
                paths = tmapService.findHospitalsWithDistanceTmap(
                        userLocation,
                        hospitals
                );
            } catch (Exception e) {
                log.error("티맵 실패: {}", e.getMessage());
            }
        }

        Map<String, PathResponseDto> map = new HashMap<>();

        if (paths != null) {
            for (PathResponseDto p : paths) {
                map.put(p.getHpid(), p);
            }
        }

        return map;
    }


    // 6. 거리/시간 계산
    private HospitalRouteInfo resolveRouteInfo(
            Hospital hospital,
            PathResponseDto path,
            Double userLat,
            Double userLon
    ) {

        if (path != null) {
            return new HospitalRouteInfo(
                    path.getDistance(),
                    path.getDuration(),
                    calculateTimeWeight(path.getDuration())
            );
        }

        if (hospital.getLatitude() == null ||
                hospital.getLongitude() == null) {
            return null;
        }

        double distance = calculateDirectDistance(
                userLat,
                userLon,
                hospital.getLatitude(),
                hospital.getLongitude()
        );

        double duration = (distance /AVG_SPEED_KMH) * 60.0;

        return new HospitalRouteInfo(
                distance,
                duration,
                calculateTimeWeight(duration)
        );
    }

    // 7. util
    private double calculateTimeWeight(double duration) {
        double weight = 20 - (duration * DURATION_WEIGHT_FACTOR);
        return Math.max(weight, 0);
    }

    private double calculateDirectDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {
        final int EARTH_RADIUS = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);

        return EARTH_RADIUS *
                (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }


    // 8. DB 조회
    private List<HospitalScore> getScoresByCategory(HospitalCategory category) {

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

        return Collections.emptyList();
    }


    // 9. 결과 출력 로그
    private void logRecommendationResult(
            List<HospitalResponseDto> result
    ) {

        log.info("====== [병원 추천 결과] ======");

        for (int i = 0; i < result.size(); i++) {

            HospitalResponseDto dto = result.get(i);

            log.info("[{}위] {} | 점수:{} | 거리:{}km | 시간:{}분",
                    i + 1,
                    dto.getHospitalName(),
                    String.format("%.2f", dto.getFinalScore()),
                    String.format("%.2f", dto.getDistance()),
                    String.format("%.1f", dto.getDuration())
            );
        }

        log.info("=============================");
    }

    // inner class
    private static class HospitalRouteInfo {

        double distance;
        double duration;
        double durationWeight;

        HospitalRouteInfo(
                double distance,
                double duration,
                double durationWeight
        ) {
            this.distance = distance;
            this.duration = duration;
            this.durationWeight = durationWeight;
        }
    }

}