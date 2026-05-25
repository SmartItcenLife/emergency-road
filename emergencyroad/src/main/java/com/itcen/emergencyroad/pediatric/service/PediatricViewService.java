package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import com.itcen.emergencyroad.recommend.dto.HospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.PediatricHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalSortType;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PediatricViewService {
    private final PediatricRealtimeRepository pediatricRealtimeRepository;
    private final HospitalRecommendationService hospitalRecommendationService;

    public PediatricHospitalDetailDto getPediatricHospitalDetail(String hpid) {
        return pediatricRealtimeRepository.findPediatricHospitalDetail(hpid)
                .orElseThrow(() -> new IllegalArgumentException("소아 병원 상세 정보가 없습니다. hpid=" + hpid));
    }

    public List<PediatricHospitalListDto> getPediatricHospitalList(Double lat, Double lon, HospitalSortType sortType) {

        List<HospitalResponseDto> recommendations =
                hospitalRecommendationService.getRecommendations(
                        HospitalCategory.PEDIATRIC,
                        lat,
                        lon,
                        false
                );

        recommendations.forEach(dto ->
                System.out.println(dto.getClass().getName())
        );

        List<PediatricHospitalListDto> hospitals = recommendations.stream()
                .filter(dto -> dto instanceof PediatricHospitalResponseDto)
                .map(dto -> (PediatricHospitalResponseDto) dto)
                .map(p -> PediatricHospitalListDto.builder()
                        .hpid(p.getHpid())
                        .hospitalName(p.getHospitalName())
                        .availablePediatricBedCount(p.getAvailablePediatricBedCount())
                        .totalPediatricBedCount(p.getTotalPediatricBedCount())
                        .emergencyPhone(p.getEmergencyPhone())
                        .hospitalLatitude(p.getHospitalLatitude())
                        .hospitalLongitude(p.getHospitalLongitude())
                        .distanceKm(p.getDistance())
                        .build()
                )
                .collect(Collectors.toList());

        sortPediatricHospitals(hospitals, sortType, lat, lon);

        return hospitals;
    }

    private void sortPediatricHospitals(
            List<PediatricHospitalListDto> hospitals,
            HospitalSortType sortType,
            Double lat,
            Double lon
    ) {
        if (sortType == HospitalSortType.BED) {
            // 소아 병상 여유순은 가용 소아 병상 비율이 높은 병원부터 보여준다.
            hospitals.sort(
                    Comparator.comparing(
                            (PediatricHospitalListDto hospital) -> hospital.getAvailableBedPercentage(),
                            Comparator.nullsLast(Comparator.reverseOrder())
                    ).thenComparing(
                            (PediatricHospitalListDto hospital) -> hospital.getAvailablePediatricBedCount(),
                            Comparator.nullsLast(Comparator.reverseOrder())
                    )
            );
        }

        if (sortType == HospitalSortType.DISTANCE) {
            // 거리순은 가까운 일부 병원만 도로거리/시간을 조회하고, 나머지는 직선거리로 보완한다.
            Map<String, PathResponseDto> routeMap =
                    hospitalRecommendationService.getDistanceAndDurationMap(
                            HospitalCategory.PEDIATRIC,
                            lat,
                            lon,
                            30
                    );

            hospitals.forEach(hospital -> {
                PathResponseDto path = routeMap.get(hospital.getHpid());

                if (path != null) {
                    hospital.updateRouteInfo(
                            path.getDistance(),
                            path.getDuration()
                    );
                }
            });

            // 가까운 병원부터 보여주기 위해 거리 오름차순으로 정렬한다.
            hospitals.sort(
                    Comparator.comparing(
                            hospital -> hospital.getDistanceKm(),
                            Comparator.nullsLast(Comparator.naturalOrder())
                    )
            );
        }
    }
}
