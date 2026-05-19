package com.itcen.emergencyroad.recommend.mapper;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.recommend.dto.PediatricHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.service.PediatricCongestionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PediatricHospitalMapper {

    private final PediatricCongestionCalculator congestionCalculator;



    public PediatricHospitalResponseDto toDto(
            HospitalScore score,
            double finalScore,
            double distance,
            double duration
    ) {

        var realtime = score.getPediatricRealtime();
        var standard = score.getPediatricStandard();
        Hospital hospital = score.getHospital();

        Integer bed = Optional.ofNullable(realtime)
                .map(PediatricRealtime::getPediatricBedCount)
                .orElse(0);

        Integer total = Optional.ofNullable(standard)
                .map(PediatricStandard::getPediatricBedStandard)
                .orElse(0);

        String incubator = Optional.ofNullable(realtime)
                .map(PediatricRealtime::getIncubatorResourceAvailable)
                .orElse("N");

        double percent = congestionCalculator.getPercentage(bed, total);
        String label = congestionCalculator.getLabel(bed, total);

        return PediatricHospitalResponseDto.builder()
                .hospitalName(hospital.getHospitalName())
                .hpid(hospital.getHpid())
                .finalScore(finalScore)
                .distance(distance)
                .duration(duration)
                .address(hospital.getAddress())

                .availablePediatricBedCount(bed)
                .totalPediatricBedCount(total)

                .incubatorAvailable(incubator)
                .congestionLabel(label)
                .availableBedPercentage(percent)

                .emergencyPhone(hospital.getEmergencyPhone())

                .hospitalLatitude(hospital.getLatitude())
                .hospitalLongitude(hospital.getLongitude())

                .tags(score.getPediatricTags())
                .build();
    }


}