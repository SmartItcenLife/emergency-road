package com.itcen.emergencyroad.recommend.mapper;

import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.recommend.dto.GeneralHospitalResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.service.PediatricCongestionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeneralHospitalMapper {
    private final PediatricCongestionCalculator congestionCalculator;

    public GeneralHospitalResponseDto toDto(
            HospitalScore score,
            double finalScore,
            double distance,
            double duration
    ) {
        Hospital hospital = score.getHospital();

        GeneralRealTimeAndStandard realtime = score.getGeneralRealTimeAndStandard();

        Integer availableBed = Optional.ofNullable(realtime)
                .map(GeneralRealTimeAndStandard::getEmergencyAvailableBeds)
                .orElse(0);

        Integer totalBed = Optional.ofNullable(realtime)
                .map(GeneralRealTimeAndStandard::getEmergencyTotalBeds)
                .orElse(0);

        double percent = congestionCalculator.getPercentage(availableBed, totalBed);
        String label = congestionCalculator.getLabel(availableBed, totalBed);

        LocalDateTime recordedAt = Optional.ofNullable(realtime)
                .map(GeneralRealTimeAndStandard::getRecordedAt)
                .orElse(null);

        return GeneralHospitalResponseDto.builder()
                .hospitalName(hospital.getHospitalName())
                .hpid(hospital.getHpid())
                .finalScore(finalScore)
                .distance(distance)
                .duration(duration)
                .address(hospital.getAddress())
                .availableEmergencyBedCount(availableBed)
                .totalEmergencyBedCount(totalBed)
                .hospitalLatitude(hospital.getLatitude())
                .hospitalLongitude(hospital.getLongitude())
                .emergencyPhone(hospital.getEmergencyPhone())
                .recordedAt(recordedAt)
                .congestionLabel(label)
                .availableBedPercentage(percent)
                .tags(score.getGeneralTags())
                .build();
    }
}
