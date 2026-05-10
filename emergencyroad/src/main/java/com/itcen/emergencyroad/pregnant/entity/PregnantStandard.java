package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

//예) 응급 대응 가능한 분만실 기준이
@Entity
@Table(name = "pregnant_standard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnantStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //병원 FK
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
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

    public void update(EmrDto dto) {
        this.hvs26= dto.getHvs26();
        this.hvs08 = dto.getHvs08();
        this.hvs31 = dto.getHvs31();
        this.hvs32 = dto.getHvs32();
    }

}