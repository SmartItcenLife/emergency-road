package com.itcen.emergencyroad.recommend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class PregnantHospitalResponseDto extends HospitalResponseDto {
    // 분만 가능 여부
    private String deliveryAvailable;

    // NICU 가능 여부 (Y/N)
    private String nicuAvailable;

    // 산과 수술 가능 여부
    private String obstetricSurgeryAvailable;

    // 부인과 수술 가능 여부
    private String gynecologySurgeryAvailable;

    // 응급투석 가능 여부
    private String emergencyDialysisAvailable;

    // 실시간 NICU 병상 수
    private Integer nicuBedCount;

    // 인큐베이터 사용 가능 여부
    private String incubatorAvailable;

    // 조산아 인공호흡기 가능 여부
    private String prematureVentilatorAvailable;

    // 현재 분만실 가용 여부
    private String isDeliveryRoomAvailable;

    // 기준 자원
    private Integer deliveryRoomStandard;
    private Integer nicuStandard;
    private Integer ventilatorStandard;
    private Integer incubatorStandard;

    //응급실 번호
    private String emergencyPhone;

    //병원 위도 경도
    private Double hospitalLatitude;
    private Double hospitalLongitude;

    private String congestionLabel;
    private Double availableBedPercentage;
}