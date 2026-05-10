package com.itcen.emergencyroad.pediatric.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pediatric_realtime")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PediatricRealtime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "pediatric_venti")
    private String pediatricVentiAvailable; // hv10, 소아 인공호흡기 가능여부

    @Column(name = "preemie_venti")
    private String preemieVentiAvailable; // hvventisoayn, 조산아용 인공호흡기 가용 여부

    @Column(name = "incubator")
    private String incubatorAvailable; // hv11, 인큐베이터(보육기) 보유/상태 관련

    @Column(name = "incubator_available")
    private String incubatorResourceAvailable; // hvincuayn, 인큐베이터 가용 여부

    @Column(name = "pediatric_hotline", length = 20)
    private String pediatricHotline; // hv12, 소아당직의 직통연락처

    @Column(name = "pediatric_pressure_isolation")
    private Integer pediatricNegativeIsolationCount; // hv15, 소아 음압격리

    @Column(name = "pediatric_isolation")
    private Integer pediatricGeneralIsolationCount; // hv16, 소아 일반격리

    @Column(name = "pediatric_bed_count")
    private Integer pediatricBedCount; // hv28, 소아 병상 수

    @Column(name = "pediatric_icu_count")
    private Integer pediatricIcuCount; // hv32, 소아 중환자실 수

    @Column(name = "pediatric_emergency_icu_count")
    private Integer pediatricEmergencyIcuCount; // hv33, 응급전용 소아중환자실 수

    @Column(name = "pediatric_emergency_admission_count")
    private Integer pediatricEmergencyAdmissionCount; // hv37, 응급전용 소아입원실 수

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt; // hvidate 변환값

    public void updateRealtimeData(
            String pediatricVentiAvailable,
            String preemieVentiAvailable,
            String incubatorAvailable,
            String incubatorResourceAvailable,
            Integer pediatricNegativeIsolationCount,
            Integer pediatricGeneralIsolationCount,
            Integer pediatricBedCount,
            Integer pediatricIcuCount,
            Integer pediatricEmergencyIcuCount,
            Integer pediatricEmergencyAdmissionCount,
            String pediatricHotline,
            LocalDateTime recordedAt
    ) {
        this.pediatricVentiAvailable = pediatricVentiAvailable;
        this.preemieVentiAvailable = preemieVentiAvailable;
        this.incubatorAvailable = incubatorAvailable;
        this.incubatorResourceAvailable = incubatorResourceAvailable;
        this.pediatricNegativeIsolationCount = pediatricNegativeIsolationCount;
        this.pediatricGeneralIsolationCount = pediatricGeneralIsolationCount;
        this.pediatricBedCount = pediatricBedCount;
        this.pediatricIcuCount = pediatricIcuCount;
        this.pediatricEmergencyIcuCount = pediatricEmergencyIcuCount;
        this.pediatricEmergencyAdmissionCount = pediatricEmergencyAdmissionCount;
        this.pediatricHotline = pediatricHotline;
        this.recordedAt = recordedAt;
    }
}
