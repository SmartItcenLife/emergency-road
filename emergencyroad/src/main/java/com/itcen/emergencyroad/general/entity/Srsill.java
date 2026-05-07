package com.itcen.emergencyroad.general.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "srsill_available_yn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Srsill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //병원 ID (FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    // 일반
    @Column(name = "MKioskTy1")
    private String MKioskTy1; // 심근경색
    @Column(name = "MKioskTy2")
    private String MKioskTy2; // 뇌경색
    @Column(name = "MKioskTy3")
    private String MKioskTy3; // 거미막하 출혈
    @Column(name = "MKioskTy4")
    private String MKioskTy4; // 거미막하출혈 외
    @Column(name = "MKioskTy5")
    private String MKioskTy5; // 대동맥응급_흉부
    @Column(name = "MKioskTy6")
    private String MKioskTy6; // 대동맥응급_복부
    @Column(name = "MKioskTy23")
    private String MKioskTy23; // 응급투석
    @Column(name = "MKioskTy24")
    private String MKioskTy24; // 폐쇄병동입원
    @Column(name = "MKioskTy11")
    private String MKioskTy11;// 응급 내시경-성인위장관
    @Column(name = "MKioskTy13")
    private String MKioskTy13;// 응급내시경-성인 기관지
    @Column(name = "MKioskTy19")
    private String MKioskTy19;// 중증화상-전문치료
    @Column(name = "MKioskTy26")
    private String MKioskTy26;// 영상의학혈관중재-성인

//    // 임산부
//    @Column(name = "MKioskTy22")
//    private String MKioskTy22; // 응급투석
//    //private String MKioskTy15; // 저체중출생아 집중치료
//    @Column(name = "MKioskTy16")
//    private String MKioskTy16; // 산부인과응급 분만
//    @Column(name = "MKioskTy17")
//    private String MKioskTy17; // 산부인과응급 산과수술
//    @Column(name = "MKioskTy18")
//    private String MKioskTy18; // 산부인과응급 부인과수술
//
//    // 소아
//    @Column(name = "MKioskTy10")
//    private String MKioskTy10; // 장충첩/폐색_영유아
//    @Column(name = "MKioskTy12")
//    private String MKioskTy12; // 응급내시경_영유아 위장관
//    @Column(name = "MKioskTy14")
//    private String MKioskTy14; // 응급내시경_영유아_기관지
//    @Column(name = "MKioskTy15")
//    private String MKioskTy15; // 저체중 출산아
//    @Column(name = "MKioskTy27")
//    private String MKioskTy27; // 영상의학혈관중재_영유아


}
