package com.itcen.emergencyroad.general.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_standard_new")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GeneralRealTimeAndStandard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA에서 사용하는 기본키 ID

    //병원 ID (FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    // --- 응급실 병상 정보 ---
    @Column(name = "er_available_beds")
    private Integer hvec; // 응급실 일반 병상 실시간 가용 병상 수

    @Column(name = "er_total_beds")
    private Integer hvs01; // 응급실 일반 병상 전체 병상 수

    // --- 중환자실 병상 정보 ---
    @Column(name = "icu_available_beds")
    private Integer hvicc; // 일반 중환자실 실시간 가용 병상 수

    @Column(name = "icu_total_beds")
    private Integer hvs17; // 일반 중환자실 전체 병상 수

    @Column(name = "neuro_icu_available_beds")
    private Integer hvcc; // 신경과 중환자실 실시간 가용 병상 수

    @Column(name = "neuro_icu_total_beds")
    private String hvs11; // 신경과 중환자실 전체 병상 수

    @Column(name = "chest_icu_available_beds")
    private Integer hvccc; // 흉부외과 중환자실 실시간 가용 병상 수

    @Column(name = "chest_icu_total_beds")
    private Integer hvs16; // 흉부외과 중환자실 전체 병상 수

    // --- 응급실 장비 가용 여부 ---
    @Column(name = "ct_yn")
    private String hvctayn; // CT 촬영 가능 여부

    @Column(name = "mri_yn")
    private String hvmariayn; // MRI 촬영 가능 여부

    @Column(name = "ventilator_yn")
    private String hvventiayn; // 인공호흡기 사용 가능 여부

    @Column(name = "crrt_yn")
    private String hvcrrtayn; // 지속적 신대체요법(CRRT) 가능 여부

    @Column(name = "ecmo_yn")
    private String hvecmoayn; // ECMO 장비 사용 가능 여부

    @Column(name = "angio_yn")
    private String hvangioayn; // 혈관조영술 가능 여부

}