package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pregnant_realtime_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnantRealtimeStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //병원 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    //분만실 기준
    @Column(name = "HVS26")
    private Integer hvs26;

    // 신생아 중환자실 기준
    @Column(name = "HVS08")
    private Integer hvs08;

    //인공호흡기 기준
    @Column(name = "HVS31")
    private Integer hvs31;

    //인큐베이터 기준
    @Column(name = "HVS32")
    private Integer hvs32;
}