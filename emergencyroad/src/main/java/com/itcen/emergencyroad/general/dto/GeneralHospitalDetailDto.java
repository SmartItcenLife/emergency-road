package com.itcen.emergencyroad.general.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GeneralHospitalDetailDto {

    private String hpid;

    // 기본 정보
    private String address;
    private String emergencyPhone; // 응급실 번호
    private String phone; // 대표 전화

    // 응급실 병상
    private Integer emergencyAvailableBeds;
    private Integer emergencyTotalBeds;

    // 중환자실 병상
    private Integer icuAvailableBeds;
    private Integer icuTotalBeds;
    private Integer neuroIcuAvailableBeds;
    private String neuroIcuTotalBeds;
    private Integer chestIcuAvailableBeds;
    private Integer chestIcuTotalBeds;

    // 장비 가능 여부
    private String ctAvailable;
    private String mriAvailable;
    private String ventilatorAvailable;
    private String crrtAvailable;
    private String ecmoAvailable;
    private String angioAvailable;

    // 중증질환 수용 가능 여부
    private String myocardialInfarctionAvailable;
    private String cerebralInfarctionAvailable;
    private String subarachnoidHemorrhageAvailable;
    private String otherHemorrhageAvailable;
    private String aorticChestAvailable;
    private String aorticAbdomenAvailable;
    private String dialysisAvailable;
    private String closedWardAvailable;
    private String endoscopyGiAvailable;
    private String endoscopyBronchialAvailable;
    private String severeBurnsAvailable;
    private String angioAdultAvailable;

    private LocalDateTime realtimeRecordedAt;
}