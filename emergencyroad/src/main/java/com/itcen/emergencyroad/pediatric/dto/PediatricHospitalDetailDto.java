package com.itcen.emergencyroad.pediatric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PediatricHospitalDetailDto {

    private String hpid;

    // 상세보기 - 기본정보 탭
    private String address;
    private String emergencyPhone;
    private String phone;
//    private String departmentNames; // 진료과목 - 현재 이는 제외

    // 실시간 소아 자원
    private Integer pediatricIcuCount;
    private Integer pediatricEmergencyIcuCount;
    private Integer pediatricEmergencyAdmissionCount;
    private Integer pediatricNegativeIsolationCount;
    private Integer pediatricGeneralIsolationCount;

    // 기준 소아 자원
    private Integer pediatricIcuStandard;
    private Integer pediatricEmergencyIcuStandard;
    private Integer pediatricEmergencyAdmissionStandard;
    private Integer pediatricNegativeIsolationStandard;
    private Integer pediatricGeneralIsolationStandard;

    // 장비 가능 여부
    private String pediatricVentiAvailable;
    private String preemieVentiAvailable;
    private String incubatorAvailable;
    private String incubatorResourceAvailable;
    private String pediatricHotline;

    // 소아 중증 수용 가능 여부
    private String pediatricBowelObstructionAvailable;
    private String pediatricEmergencyEndoscopyGastroAvailable;
    private String pediatricEmergencyEndoscopyBronchialAvailable;
    private String lowBirthWeightInfantAvailable;
    private String pediatricVascularInterventionAvailable;

    // 메시지
    private String pediatricBowelObstructionMessage;
    private String pediatricEmergencyEndoscopyGastroMessage;
    private String pediatricEmergencyEndoscopyBronchialMessage;
    private String lowBirthWeightInfantMessage;
    private String pediatricVascularInterventionMessage;

    // 갱신 시간
    private LocalDateTime realtimeRecordedAt;
    private LocalDateTime standardRecordedAt;
}