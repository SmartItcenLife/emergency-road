package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

/** 병원이 응급 상황에서 원래 운영 가능한 기준(capacity) 정보
  * getEmrrmRltmUsefulSckbdInfoInqire
  */
@Entity
@Table(name = "pregnant_standard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnantStandard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원 FK
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    // 응급 대응 가능한 분만실 기준 수
    @Column(name = "HVS26")
    private Integer deliveryRoomStandard;

    // 응급 대응 가능한 NICU 기준 수
    @Column(name = "HVS08")
    private Integer nicuStandard;

    // 응급 대응 가능한 조산아 인공호흡기 기준 수
    @Column(name = "HVS31")
    private Integer ventilatorStandard;

    // 응급 대응 가능한 인큐베이터 기준 수
    @Column(name = "HVS32")
    private Integer incubatorStandard;

    public void update(EmrDto dto) {
        this.deliveryRoomStandard = dto.getHvs26();
        this.nicuStandard = dto.getHvs08();
        this.ventilatorStandard = dto.getHvs31();
        this.incubatorStandard = dto.getHvs32();
    }
}