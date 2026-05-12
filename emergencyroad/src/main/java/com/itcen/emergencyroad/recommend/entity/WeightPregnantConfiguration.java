package com.itcen.emergencyroad.recommend.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//비즈니스 정책

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightPregnantConfiguration extends BaseEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private HospitalCategory category;

    // 핵심 가중치
    @Builder.Default
    private Double deliveryAvailableWeight = 40.0;      // 분만 가능

    @Builder.Default
    private Double obstetricSurgeryWeight = 20.0;       // 산과 수술

    @Builder.Default
    private Double nicuAvailableWeight = 10.0;          // NICU 보유


    // 실시간 상태 가중치
    @Builder.Default
    private Double deliveryRoomAvailableWeight = 30.0;  // 분만실 여유

    @Builder.Default
    private Double emergencyRoomAvailableWeight = 30.0; // 응급실 가용성


    // 구조 기준값
    @Builder.Default
    private Integer operatingRoomThreshold = 3;         // 수술실 기준 개수

    // 장비 / 추가 가중치
    @Builder.Default
    private Double incubatorWeight = 10.0;              // 인큐베이터 가점

    @Builder.Default
    private Double prematureVentilatorWeight = 5.0;     // 조산아 호흡기 가점

    @Builder.Default
    private Double operatingRoomBonusWeight = 5.0;      // 수술실 보너스

    @Builder.Default
    private Double nicuScaleWeight = 2.0;
    @Builder.Default
    private Double maxNicuScaleScore = 10.0;// NICU 병상 1개당 점수

    public void updatePregnantWeights(
            Double delivery,
            Double surgery,
            Double nicu,
            Double deliveryRoom,
            Double emergencyRoom,
            Integer operatingRoom,
            Double incubator,
            Double ventilator,
            Double operatingRoomBonus,
            Double nicuScaleWeight,
            Double maxNicuScaleScore
    ) {
        this.deliveryAvailableWeight = delivery;
        this.obstetricSurgeryWeight = surgery;
        this.nicuAvailableWeight = nicu;

        this.deliveryRoomAvailableWeight = deliveryRoom;
        this.emergencyRoomAvailableWeight = emergencyRoom;

        this.operatingRoomThreshold = operatingRoom;

        this.incubatorWeight = incubator;
        this.prematureVentilatorWeight = ventilator;
        this.operatingRoomBonusWeight = operatingRoomBonus;
        this.nicuScaleWeight = nicuScaleWeight;
        this.maxNicuScaleScore = maxNicuScaleScore;
    }
}