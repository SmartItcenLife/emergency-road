package com.itcen.emergencyroad.general.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GeneralSrsIllDTO {
    private String hpid;

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

}
