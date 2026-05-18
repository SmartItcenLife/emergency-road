package com.itcen.emergencyroad.external.dto;

import lombok.*;

//공통 DTO for 응급실 실시간 가용병상정보 조회 오퍼레이션 1번 API
/* 이 곳에 필요한 응답 필드 명시 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmrDto {

    private String hpid;
    private String phpid; //구 코드

    //병원 정보
    private String dutyName; //병원 이름
    private String dutyTel3; //대표 번호

    //임산부
    private String hv42;       // 분만실
    private Integer hvncc;      // NICU
    private String hv11;        // 인큐베이터 여부
    private String hvincuayn;   // 인큐베이터 가용
    private String hvventisoayn; // 인공호흡기

    //임산부 - standard
    private Integer hvs26;
    private Integer hvs08;
    private Integer hvs31;
    private Integer hvs32;


    // 일반
    private Integer hvec; /// 응급실 일반 병상 실시간 가용 병상 수
    private Integer hvs01; // 응급실 일반 병상 전체 병상 수
    private Integer hvicc; // 일반 중환자실 실시간 가용 병상 수
    private Integer hvs17; // 일반 중환자실 전체 병상 수
    private Integer hvcc; // 신경과 중환자실 실시간 가용 병상 수
    private String hvs11; // 신경과 중환자실 전체 병상 수
    private Integer hvccc; // 흉부외과 중환자실 실시간 가용 병상 수
    private Integer hvs16; // 흉부외과 중환자실 전체 병상 수

    private String hvctayn;
    private String hvmariayn;
    private String hvventiayn;
    private String hvcrrtayn;
    private String hvecmoayn;
    private String hvangioayn;

    private String hvidate; // 입력일시

    // 중증 질환
    // 일반
    private String MKioskTy1; // 심근경색
    private String MKioskTy2; // 뇌경색
    private String MKioskTy3; // 거미막하 출혈
    private String MKioskTy4; // 거미막하출혈 외
    private String MKioskTy5; // 대동맥응급_흉부
    private String MKioskTy6; // 대동맥응급_복부
    private String MKioskTy23; // 응급투석
    private String MKioskTy24; // 폐쇄병동입원
    private String MKioskTy11;// 응급 내시경-성인위장관
    private String MKioskTy13;// 응급내시경-성인 기관지
    private String MKioskTy19;// 중증화상-전문치료
    private String MKioskTy26;// 영상의학혈관중재-성인

    // 임산부
    private String MKioskTy22; // 응급투석
    private String MKioskTy15; // 저체중출생아 집중치료
    private String MKioskTy16; // 산부인과응급 분만
    private String MKioskTy17; // 산부인과응급 산과수술
    private String MKioskTy18; // 산부인과응급 부인과수술

    // 소아
    private String MKioskTy10; // 장충첩/폐색_영유아
    private String MKioskTy12; // 응급내시경_영유아 위장관
    private String MKioskTy14; // 응급내시경_영유아_기관지
    //private String MKioskTy15; // 저체중 출산아
    private String MKioskTy27; // 영상의학혈관중재_영유아

}
