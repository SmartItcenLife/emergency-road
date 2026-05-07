package com.itcen.emergencyroad.pregnant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PregnantAggregateDto {

    private Long hpid;

    // EmergencyRealtimeDto
    private Integer hv42;
    private Integer hvncc;
    private String hv11;
    private String hvincuayn;
    private String hvventisoayn;

    // EmergencyTreatmentCapabilityDto
    private String mkioskty15;
    private String mkioskty16;
    private String mkioskty17;
    private String mkioskty18;
    private String mkioskty22;
}