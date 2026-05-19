package com.itcen.emergencyroad.recommend.mapper;

import com.itcen.emergencyroad.recommend.dto.PregnantHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import org.springframework.stereotype.Component;

@Component
public class PregnantHospitalMapper {

    public PregnantHospitalResponseDto toDto(
            HospitalScore score,
            double finalScore,
            double distance,
            double duration
    ) {

        var hospital = score.getHospital();

        var pregnant = score.getPregnant();
        var realtime = score.getPregnantRealtime();
        var standard = score.getPregnantStandard();

        return PregnantHospitalResponseDto.builder()
                .hospitalName(hospital.getHospitalName())
                .hpid(hospital.getHpid())
                .finalScore(finalScore)
                .distance(distance)
                .duration(duration)
                .address(hospital.getAddress())
                .tags(score.getPregnantTags())

                // 가능 여부
                .deliveryAvailable(pregnant.getDeliveryAvailable())
                .nicuAvailable(pregnant.getNicuAvailable())
                .obstetricSurgeryAvailable(pregnant.getObstetricSurgeryAvailable())
                .gynecologySurgeryAvailable(pregnant.getGynecologySurgeryAvailable())
                .emergencyDialysisAvailable(pregnant.getEmergencyDialysisAvailable())

                // realtime
                .nicuBedCount(realtime.getNicuBedCount())
                .incubatorAvailable(realtime.getIncubatorAvailable())
                .prematureVentilatorAvailable(realtime.getPrematureVentilatorAvailable())
                .isDeliveryRoomAvailable(realtime.getIsDeliveryRoomAvailable())

                // standard
                .deliveryRoomStandard(standard.getDeliveryRoomStandard())
                .nicuStandard(standard.getNicuStandard())
                .ventilatorStandard(standard.getVentilatorStandard())
                .incubatorStandard(standard.getIncubatorStandard())

                .emergencyPhone(hospital.getEmergencyPhone())
                .hospitalLatitude(hospital.getLatitude())
                .hospitalLongitude(hospital.getLongitude())
                .build();
    }
}