package com.itcen.emergencyroad.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralHospitalResponseDto extends HospitalResponseDto {

    private Integer availableEmergencyBedCount;      // 가용 병상 수 (hvec)
    private Integer totalEmergencyBedCount; // 전체 병상 수 (hvs01)
    private String congestionLabel; //혼잡도
    private Double availableBedPercentage; //퍼센트
    private String emergencyPhone; // 응급실 전화번호

    private Double hospitalLatitude; // 위도

    private Double hospitalLongitude; // 경도

    private LocalDateTime recordedAt; // 데이터 수집 시각
}