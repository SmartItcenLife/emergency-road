package com.itcen.emergencyroad.pediatric.service;

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

        return recommendations.stream()
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
    }
}