package com.itcen.emergencyroad.pregnant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class PregnantHospitalDetailDto {
    private String hpid;
    private String address;
    private String emergencyPhone;
    private String phone;

    // 실시간 자원 정보
    private Integer nicuBedCount;               // NICU 병상 수
    private String isDeliveryRoomAvailable;    // 분만실 가능 여부
    private String incubatorAvailable;         // 인큐베이터 보유 여부
    private String prematureVentilatorAvailable; // 조산아 인공호흡기


    //기준 임산부 자원
    private Integer deliveryRoomStandard; // 분만실 기준 수
    private Integer nicuStandard; // NICU 기준 수
    private Integer ventilatorStandard; // 인공호흡기 기준
    private Integer incubatorStandard; // 인큐베이터 기준


    //진료 가능 여부
    private String nicuAvailable; // 저체중 출생아 치료 가능
    private String deliveryAvailable; // 분만 가능 여부
    private String obstetricSurgeryAvailable; // 산과 수술 가능
    private String gynecologySurgeryAvailable; // 부인과 수술 가능
    private String emergencyDialysisAvailable; // 응급 투석 가능

    // 시간 정보
    private LocalDateTime realtimeRecordedAt;   // 실시간 정보 갱신 시간
    private LocalDateTime standardRecordedAt;   // 기준 갱신 시간
}
