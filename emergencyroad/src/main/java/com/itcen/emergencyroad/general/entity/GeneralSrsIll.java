package com.itcen.emergencyroad.general.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_srsill_available_yn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GeneralSrsIll extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //병원 ID (FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    // --- 중증 질환 수용 여부 (일반) ---
    @Column(name = "mi_yn")
    private String MKioskTy1; // 심근경색

    @Column(name = "cerebral_infarction_yn")
    private String MKioskTy2; // 뇌경색

    @Column(name = "subarachnoid_hemorrhage_yn")
    private String MKioskTy3; // 거미막하 출혈

    @Column(name = "other_hemorrhage_yn")
    private String MKioskTy4; // 거미막하출혈 외

    @Column(name = "aortic_chest_yn")
    private String MKioskTy5; // 대동맥응급_흉부

    @Column(name = "aortic_abdomen_yn")
    private String MKioskTy6; // 대동맥응급_복부

    @Column(name = "dialysis_yn")
    private String MKioskTy23; // 응급투석

    @Column(name = "closed_ward_yn")
    private String MKioskTy24; // 폐쇄병동입원

    @Column(name = "endoscopy_gi_yn")
    private String MKioskTy11; // 응급 내시경-성인위장관

    @Column(name = "endoscopy_bronchial_yn")
    private String MKioskTy13; // 응급내시경-성인 기관지

    @Column(name = "severe_burns_yn")
    private String MKioskTy19; // 중증화상-전문치료

    @Column(name = "angio_adult_yn")
    private String MKioskTy26; // 영상의학혈관중재-성인

}
