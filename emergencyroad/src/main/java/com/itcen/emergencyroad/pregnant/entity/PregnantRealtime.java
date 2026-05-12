package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

//실시간 현재 남아있는 수용 현황
@Entity
@Table(name = "pregnant_realtime")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PregnantRealtime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    //병원이 인큐베이터 자체를 보유 중인가 인큐베이터 보육기 여부 (Y/N) getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hv11", length = 20)
    private String incubatorExists;

    // 현재 분만실 가용 여부
    @Column(name = "hv42")
    private String isDeliveryRoomAvailable;

    // 현재 NICU 병상 수
    @Column(name = "hvncc")
    private Integer nicuBedCount;

    // 현재 인큐베이터 가용 여부
    @Column(name = "hvincuayn", length = 20)
    private String incubatorAvailable;

    // 현재 조산아 인공호흡기 가용 여부
    @Column(name = "hvventisoayn", length = 20)
    private String prematureVentilatorAvailable;

    public void update(EmrDto dto) {
        this.incubatorExists= dto.getHv11();
        this.isDeliveryRoomAvailable = dto.getHv42();
        this.nicuBedCount = dto.getHvncc();
        this.incubatorAvailable = dto.getHvincuayn();
        this.prematureVentilatorAvailable = dto.getHvventisoayn();
    }
}