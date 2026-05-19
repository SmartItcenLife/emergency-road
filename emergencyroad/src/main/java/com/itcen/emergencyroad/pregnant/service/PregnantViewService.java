package com.itcen.emergencyroad.pregnant.service;

import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalListDto;
import com.itcen.emergencyroad.pregnant.repository.PregnantRealtimeRepository;
import com.itcen.emergencyroad.recommend.dto.HospitalResponseDto;
import com.itcen.emergencyroad.recommend.dto.PregnantHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PregnantViewService {
    private final PregnantRealtimeRepository pregnantRealtimeRepository;
    private final HospitalRecommendationService hospitalRecommendationService;

    public PregnantHospitalDetailDto findPregnantRealtimeByHospital(String hpid) {
        return pregnantRealtimeRepository.findPregnantHospitalDetail(hpid)
                .orElseThrow(() -> new IllegalArgumentException("임산부 병원 상세 정보가 없습니다. hpid=" + hpid));
    }

    // 전체 리스트 조회
    public List<PregnantHospitalListDto> getPregnantHospitalList(
            Double lat,
            Double lon
    ) {

        List<HospitalResponseDto> recommendations =
                hospitalRecommendationService.getRecommendations(
                        HospitalCategory.PREGNANT,
                        lat,
                        lon,
                        false
                );

        return recommendations.stream()
                .filter(dto ->
                        dto instanceof PregnantHospitalResponseDto)

                .map(dto ->
                        (PregnantHospitalResponseDto) dto)

                .map(pDto ->
                        PregnantHospitalListDto.builder()
                                .hpid(pDto.getHpid())
                                .hospitalName(pDto.getHospitalName())
                                .deliveryAvailable(
                                        pDto.getDeliveryAvailable())
                                .isDeliveryRoomAvailable(
                                        pDto.getIsDeliveryRoomAvailable())
                                .nicuBedCount(
                                        pDto.getNicuBedCount())
                                .nicuStandard(
                                        pDto.getNicuStandard())
                                .emergencyPhone(
                                        pDto.getEmergencyPhone())
                                .hospitalLatitude(
                                        pDto.getHospitalLatitude())
                                .hospitalLongitude(
                                        pDto.getHospitalLongitude())
                                .distanceKm(
                                        pDto.getDistance())
                                .build()
                )
                .collect(Collectors.toList());
    }
}
