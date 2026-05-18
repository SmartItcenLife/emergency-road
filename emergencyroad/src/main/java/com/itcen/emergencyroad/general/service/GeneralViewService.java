package com.itcen.emergencyroad.general.service;

import com.itcen.emergencyroad.general.dto.GeneralHospitalDetailDto;
import com.itcen.emergencyroad.general.dto.GeneralHospitalListDto;
import com.itcen.emergencyroad.general.repository.GeneralRepository;
import com.itcen.emergencyroad.recommend.dto.GeneralHospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.HospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.PediatricHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralViewService {
    private final GeneralRepository generalRepository;
    private final HospitalRecommendationService hospitalRecommendationService;

    // 현재 일반 유형 대상 추천 로직 미구현 상태로 임시로 구현하였습니다.
    public List<GeneralHospitalListDto> getGeneralHospitalList(
            Double lat,
            Double lon
    ) {

        List<HospitalResponseDto> recommendations =
                hospitalRecommendationService.getRecommendations(
                        HospitalCategory.GENERAL,
                        lat,
                        lon
                );
        recommendations.forEach(dto ->
                System.out.println(
                        dto.getClass().getName()
                ));

        return recommendations.stream()
                .filter(dto ->
                        dto instanceof GeneralHospitalResponseDto)
                .map(dto -> {

                    GeneralHospitalResponseDto gDto =
                            (GeneralHospitalResponseDto) dto;
                    System.out.println(
                            gDto.getHospitalName()
                                    + " | "
                                    + gDto.getAvailableEmergencyBedCount()
                                    + " | "
                                    + gDto.getTotalEmergencyBedCount()
                                    + " | "
                                    + gDto.getDistance()
                                    + " | "
                                    + gDto.getDuration()
                    );
                    return GeneralHospitalListDto.builder()
                            .hpid(gDto.getHpid())
                            .hospitalName(
                                    gDto.getHospitalName()
                            )
                            .availableEmergencyBedCount(
                                    gDto.getAvailableEmergencyBedCount()
                            )
                            .totalEmergencyBedCount(
                                    gDto.getTotalEmergencyBedCount()
                            )
                            .emergencyPhone(
                                    gDto.getEmergencyPhone()
                            )
                            .hospitalLatitude(
                                    gDto.getHospitalLatitude()
                            )
                            .hospitalLongitude(
                                    gDto.getHospitalLongitude()
                            )
                            .distanceKm(
                                    gDto.getDistance()
                            )
                            .duration(gDto.getDuration())
                            .recordedAt(
                                    gDto.getRecordedAt()
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    public GeneralHospitalDetailDto getGeneralHospitalDetail(String hpid) {
        return generalRepository.findGeneralHospitalDetail(hpid)
                .orElseThrow(() -> new IllegalArgumentException("일반 병원 상세 정보가 없습니다. hpid=" + hpid));
    }
}
