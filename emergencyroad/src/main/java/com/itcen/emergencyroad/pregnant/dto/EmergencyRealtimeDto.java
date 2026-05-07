package com.itcen.emergencyroad.pregnant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmergencyRealtimeDto {
//병상/장비
    private String hpid;

    private String  hv42;       // 분만실
    private Integer hvncc;      // NICU

    private String hv11;        // 인큐베이터 여부
    private String hvincuayn;   // 인큐베이터 가용
    private String hvventisoayn; // 인공호흡기
}