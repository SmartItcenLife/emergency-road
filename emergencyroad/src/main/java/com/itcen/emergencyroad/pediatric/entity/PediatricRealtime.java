package com.itcen.emergencyroad.pediatric.entity;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pediatric_realtime")
@Getter
@NoArgsConstructor
public class PediatricRealtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "pediatric_venti")
    private Boolean pediatricVentiAvailable; // hv10, 소아 인공호흡기 가능여부

    @Column(name = "preemie_venti")
    private Boolean preemieVentiAvailable; // hvventisoayn, 조산아용 인공호흡기 가용 여부

    @Column(name = "incubator")
    private Boolean incubatorAvailable; // hv11, 인큐베이터(보육기) 보유/상태 관련

    @Column(name = "incubator_available")
    private Boolean incubatorResourceAvailable; // hvincuayn, 인큐베이터 가용 여부

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
}