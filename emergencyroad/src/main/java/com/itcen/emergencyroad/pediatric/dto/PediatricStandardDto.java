package com.itcen.emergencyroad.pediatric.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PediatricStandardDto {
    private String hpid;      // 기관코드
    private String dutyName;  // 기관명

    @JsonProperty("hvs02")
    private String hvs02;     // 소아 병상수 기준

    @JsonProperty("hvs08")
    private String hvs08;     // 신생아 중환자실 기준

    @JsonProperty("hvs09")
    private String hvs09;     // 소아 중환자실 기준

    @JsonProperty("hvs10")
    private String hvs10;     // 응급전용 소아 중환자실 기준

    @JsonProperty("hvs20")
    private String hvs20;     // 응급전용 소아 입원실 기준

    @JsonProperty("hvs30")
    private String hvs30;     // 인공호흡기 일반소아 기준

    @JsonProperty("hvs31")
    private String hvs31;     // 인공호흡기 조산아 기준

    @JsonProperty("hvs32")
    private String hvs32;     // 인큐베이터 기준

    @JsonProperty("hvs48")
    private String hvs48;     // 소아 음압격리실 기준

    @JsonProperty("hvs49")
    private String hvs49;     // 소아 일반격리실 기준

    private String hvidate;   // 입력일시
}
