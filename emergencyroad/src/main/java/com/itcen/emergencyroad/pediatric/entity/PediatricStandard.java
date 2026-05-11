package com.itcen.emergencyroad.pediatric.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pediatric_standard")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PediatricStandard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "pediatric_bed_standard")
    private Integer pediatricBedStandard; // HVS02, 소아 병상수 기준

    @Column(name = "newborn_icu_standard")
    private Integer newbornIcuStandard; // HVS08, 신생아 중환자실 기준

    @Column(name = "pediatric_icu_standard")
    private Integer pediatricIcuStandard; // HVS09, 소아 중환자실 기준

    @Column(name = "pediatric_emergency_icu_standard")
    private Integer pediatricEmergencyIcuStandard; // HVS10, 응급전용 소아 중환자실 기준

    @Column(name = "pediatric_emergency_admission_standard")
    private Integer pediatricEmergencyAdmissionStandard; // HVS20, 응급전용 소아 입원실 기준

    @Column(name = "general_pediatric_venti_standard")
    private Integer generalPediatricVentiStandard; // HVS30, 인공호흡기 일반소아 기준

    @Column(name = "preemie_venti_standard")
    private Integer preemieVentiStandard; // HVS31, 인공호흡기 조산아 기준

    @Column(name = "incubator_standard")
    private Integer incubatorStandard; // HVS32, 인큐베이터 기준

    @Column(name = "pediatric_negative_isolation_standard")
    private Integer pediatricNegativeIsolationStandard; // HVS48, 소아 음압격리실 기준

    @Column(name = "pediatric_general_isolation_standard")
    private Integer pediatricGeneralIsolationStandard; // HVS49, 소아 일반격리실 기준

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt; // hvidate, 입력일시

    public void updateStandardData(
            Integer pediatricBedStandard,
            Integer newbornIcuStandard,
            Integer pediatricIcuStandard,
            Integer pediatricEmergencyIcuStandard,
            Integer pediatricEmergencyAdmissionStandard,
            Integer generalPediatricVentiStandard,
            Integer preemieVentiStandard,
            Integer incubatorStandard,
            Integer pediatricNegativeIsolationStandard,
            Integer pediatricGeneralIsolationStandard,
            LocalDateTime recordedAt
    ) {
        this.pediatricBedStandard = pediatricBedStandard;
        this.newbornIcuStandard = newbornIcuStandard;
        this.pediatricIcuStandard = pediatricIcuStandard;
        this.pediatricEmergencyIcuStandard = pediatricEmergencyIcuStandard;
        this.pediatricEmergencyAdmissionStandard = pediatricEmergencyAdmissionStandard;
        this.generalPediatricVentiStandard = generalPediatricVentiStandard;
        this.preemieVentiStandard = preemieVentiStandard;
        this.incubatorStandard = incubatorStandard;
        this.pediatricNegativeIsolationStandard = pediatricNegativeIsolationStandard;
        this.pediatricGeneralIsolationStandard = pediatricGeneralIsolationStandard;
        this.recordedAt = recordedAt;
    }
}