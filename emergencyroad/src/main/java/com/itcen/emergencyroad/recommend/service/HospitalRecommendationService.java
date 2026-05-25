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
//    private Map<String, PathResponseDto> findDistanceAndDuration(
//            List<HospitalScore> baseScores,
//            Double lat,
//            Double lon
//    ) {
//
//        LocationRequestDto userLocation =
//                new LocationRequestDto(lat, lon);
//
//        List<Hospital> hospitals = new ArrayList<>();
//        for (HospitalScore score : baseScores) {
//            hospitals.add(score.getHospital());
//        }
//
//        List<PathResponseDto> paths = null;
//
//        try {
//            paths = cacaoService.findHospitalsWithDistance(
//                    userLocation,
//                    hospitals
//            );
//        } catch (Exception e) {
//            log.error("카카오 실패: {}", e.getMessage());
//        }
//
//        if (paths == null || paths.isEmpty()) {
//            try {
//                paths = tmapService.findHospitalsWithDistanceTmap(
//                        userLocation,
//                        hospitals
//                );
//            } catch (Exception e) {
//                log.error("티맵 실패: {}", e.getMessage());
//            }
//        }
//
//        Map<String, PathResponseDto> map = new HashMap<>();
//
//        if (paths != null) {
//            for (PathResponseDto p : paths) {
//                map.put(p.getHpid(), p);
//            }
//        }
//
//        return map;
//    }
    /*
     5. 거리계산 함수 v2
     기존 코드 : API 호출 실패 시 다음 API 호출 로직과정에서 모든 결과가 실패일 경우에만 동작됨
     신규 코드 : 단계별 API 호출 결과를 담아 단계별로 진행 후 리턴
     */
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

        Map<String, PathResponseDto> routeMap = new HashMap<>();

        try {
            // 1차로 카카오 다중 목적지 API를 호출하고, 성공한 병원만 routeMap에 저장
            List<PathResponseDto> kakaoPaths =
                    cacaoService.findHospitalsWithDistance(
                            userLocation,
                            hospitals
                    );
            if (kakaoPaths != null) {
                for (PathResponseDto path : kakaoPaths) {
                    routeMap.put(path.getHpid(), path);
                }
            }
        } catch (Exception e) {
            log.error("카카오 길찾기 실패 : {}", e.getMessage());
        }

        // 카카오가 일부만 혹은 전부 실패한 경우 Tmap을 이용하여 실패/누락 병원의 거리 및 시간을 계산
        List<Hospital> missingHospitals = hospitals.stream()
                .filter(hospital -> hospital.getLatitude() != null)
                .filter(hospital -> hospital.getLongitude() != null)
                .filter(hospital -> !routeMap.containsKey(hospital.getHpid()))
                .toList();

        if (!missingHospitals.isEmpty()) {
            try {
                // 2차로 Tmap은 카카오에서 누락된 병원만 호출한다.
                List<PathResponseDto> tmapPaths =
                        tmapService.findHospitalsWithDistanceTmap(
                                userLocation,
                                missingHospitals
                        );
                if (tmapPaths != null) {
                    for (PathResponseDto path : tmapPaths) {
                        routeMap.put(path.getHpid(), path);
                    }
                }
            } catch (Exception e) {
                log.error("티맵 길찾기 실패: {}", e.getMessage());
            }
        }
        return routeMap;
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
    // 7. 전체보기 병원 리스트
    public Map<String, PathResponseDto> getDistanceAndDurationMap(
            HospitalCategory category,
            Double lat,
            Double lon,
            int limit
    ){
        // 전체보기 목록은 유지해야 하므로 좌표가 있는 전체 후보를 먼저 보관한다.
        // limit은 외부 API 호출 대상에만 적용하고, 직선거리 fallback은 전체 후보에 적용한다.
        List<HospitalScore> allScores = getScoresByCategory(category).stream()
                .filter(score -> score.getHospital().getLatitude() != null)
                .filter(score -> score.getHospital().getLongitude() != null)
                .toList();

        // 외부 API 호출량을 줄이기 위해 사용자 위치 기준 직선거리로 가까운 병원만 선별한다.
        // 실제 도로거리/소요시간은 이 선별된 병원에 대해서만 카카오/Tmap으로 보완한다.
        List<HospitalScore> apiTargetScores = allScores.stream()
                .sorted(Comparator.comparing(score ->
                        calculateDirectDistance(
                                lat,
                                lon,
                                score.getHospital().getLatitude(),
                                score.getHospital().getLongitude()
                        )
                ))
                .limit(limit)
                .toList();
        Map<String, PathResponseDto> routeMap =
                findDistanceAndDuration(apiTargetScores, lat, lon);

        // API 대상 밖이거나 두 API 모두 실패한 병원은 직선거리로 보완한다.
        // 전체보기 거리순에서 병원이 누락되지 않게 하기 위한 fallback이며, 시간은 아직 계산하지 않는다.
        for (HospitalScore score : allScores) {
            Hospital hospital = score.getHospital();

            if (routeMap.containsKey(hospital.getHpid())) {
                continue;
            }

            HospitalRouteInfo routeInfo =
                    resolveRouteInfo(
                            hospital,
                            // API 결과가 없음을 명시해서 resolveRouteInfo 내부의 직선거리 계산 경로를 사용한다.
                            null,
                            lat,
                            lon
                    );

            if (routeInfo == null) {
                continue;
            }

            routeMap.put(
                    hospital.getHpid(),
                    PathResponseDto.builder()
                            .hospitalName(hospital.getHospitalName())
                            .hpid(hospital.getHpid())
                            .distance(Math.round(routeInfo.distance * 10) / 10.0)
                            .duration(0)
                            .build()
            );
        }

        return routeMap;
    }

    // 8. util
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
