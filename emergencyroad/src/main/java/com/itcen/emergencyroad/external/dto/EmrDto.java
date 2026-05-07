package com.itcen.emergencyroad.external.dto;

import lombok.*;

//공통 DTO for 응급실 실시간 가용병상정보 조회 오퍼레이션
/* 이 곳에 필요한 응답 필드 명시 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmrDto {

    private String hpid;

    //병원 정보
    private String dutyName; //병원 이름
    private String dutyTel3; //대표 번호

    //임산부
    private String hv42;       // 분만실
    private Integer hvncc;      // NICU
    private String hv11;        // 인큐베이터 여부
    private String hvincuayn;   // 인큐베이터 가용
    private String hvventisoayn; // 인공호흡기

    // 일반
    private String hvec; /// 응급실 일반 병상 실시간 가용 병상 수
    private String hvs01; // 응급실 일반 병상 전체 병상 수
    private String hvicc; // 일반 중환자실 실시간 가용 병상 수
    private String hvs17; // 일반 중환자실 전체 병상 수
    private String hvcc; // 신경과 중환자실 실시간 가용 병상 수
    private String hvs11; // 신경과 중환자실 전체 병상 수
    private String hvsccc; // 흉부외과 중환자실 실시간 가용 병상 수
    private String hvs16; // 흉부외과 중환자실 전체 병상 수


}
