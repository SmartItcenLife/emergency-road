package com.itcen.emergencyroad.external.dto;

import lombok.*;

//5번 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgytBassDto {

    private String hpid;   // 병원 고유 ID
    private String dutyName;  // 병원명

    private String dutyAddr;  // 주소
    private String dutyTel1;  // 대표 전화번호
    private String dutyTel3;   // 응급실 전화번호
    private Double wgs84Lat;   // 위도
    private Double wgs84Lon;    // 경도

    private String dgidIdName;    // 진료과목
    private Boolean dutyEryn;    // 응급실 운영 여부
    private Integer hperyn;    // 응급실 병상 수
    private Integer hvec;    // 응급실 가용 병상 수
    private Integer hpicuyn;    // 일반 중환자실
    private Integer hpnicuyn;  //신생아 중환자실
    private Integer hpopyn;    // 수술실 수

    private String dutyTime1s;    // 월요일 시작 시간
    private String dutyTime1c;    // 월요일 종료 시간
    private String dutyTime2s;    // 화요일 시작 시간
    private String dutyTime2c;    // 화요일 종료 시간
    private String dutyTime3s;    // 수요일 시작 시간
    private String dutyTime3c;    // 수요일 종료 시간
    private String dutyTime4s;    // 목요일 시작 시간
    private String dutyTime4c;    // 목요일 종료 시간
    private String dutyTime5s;    // 금요일 시작 시간
    private String dutyTime5c;    // 금요일 종료 시간
    private String dutyTime6s;    // 토요일 시작 시간
    private String dutyTime6c;    // 토요일 종료 시간
    private String dutyTime7s;    // 일요일 시작 시간
    private String dutyTime7c;    // 일요일 종료 시간
}