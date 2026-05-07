package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "general_standard")
@Getter
@NoArgsConstructor
public class GeneralStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "emergency_bed_standard")
    private Integer emergencyBedStandard; // HVS01, 응급실 병상수 기준

    @Column(name = "adult_negative_isolation_standard")
    private Integer adultNegativeIsolationStandard; // HVS17, 성인 음압격리실 기준

    @Column(name = "adult_icu_standard")
    private Integer adultIcuStandard; // HVS11, 성인 중환자실 기준

    @Column(name = "adult_general_isolation_standard")
    private Integer adultGeneralIsolationStandard; // HVS16, 성인 일반격리실 기준

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt; // hvidate, 입력일시
}