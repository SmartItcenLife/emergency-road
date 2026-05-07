package com.itcen.emergencyroad.pregnant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmergencyTreatmentCapabilityDto {
//진료 가능 여부
    private Long hpid;

    private String mkioskty15; // 저체중아 ICU
    private String mkioskty16; // 분만 가능
    private String mkioskty17; // 산과 수술
    private String mkioskty18; // 부인과 수술
    private String mkioskty22; // 응급투석
}
