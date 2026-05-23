package com.itcen.emergencyroad.recommend.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HospitalCategory category;




    /* 1. 수용 가능성
    임산부 응급 상황에서 "현재 받을 수 있는 병원인가?" 를 판단하는 영역
    * */
    @Builder.Default
    private Double deliveryWeight = 20.0; //   분만 가능 여부

    @Builder.Default
    private Double emergencyCapacityWeight = 10.0;  //응급실 가용성

    @Builder.Default
    private Double deliveryRoomWeight = 10.0; //분만실 가능 여부


    // 2. 의료 역량 : 임산부 응급 대응 능력
    @Builder.Default
    private Double obstetricSurgeryWeight = 10.0; //산과 수술 가능 여부

    @Builder.Default
    private Double nicuWeight = 10.0;   // 신생아 집중치료실 존재 여부

    @Builder.Default
    private Double incubatorWeight = 5.0; // 인큐베이터 보유

    @Builder.Default
    private Double ventilatorWeight = 5.0;  // 조산아 인공호흡기 보유

    @Builder.Default
    private Double operatingRoomWeight = 10.0; // 수술실


    // 3. 특수 대응 :    고위험 산모, 중증 산과 응급 대응
    @Builder.Default
    private Double highRiskPregnantWeight = 20.0; // 고위험 산모 대응 역량


    @Builder.Default
    private Integer operatingRoomThreshold = 3; // 충분한 산과 수술 역량으로 인정할 최소 수술실 개수

    @Builder.Default
    private Double maxNicuScaleScore = 10.0;     // NICU 병상 규모 점수 최대 제한 특정 대형병원으로 점수 쏠림 방지
}
//
//    public void updatePregnantWeights(Double availability,
//                              Double medical,
//                              Double special,
//                              Integer operatingRoomThreshold,
//                              Double maxNicuScaleScore){
//
//                this.availabilityWeight = availability;
//                this.medicalWeight = medical;
//                this.specialWeight = special;
//                this.operatingRoomThreshold = operatingRoomThreshold;
//                this.maxNicuScaleScore = maxNicuScaleScore;
//    }


//    // 핵심 가중치
//    @Builder.Default
//    private Double deliveryAvailableWeight = 40.0;      // 분만 가능
//
//    @Builder.Default
//    private Double obstetricSurgeryWeight = 20.0;       // 산과 수술
//
//    @Builder.Default
//    private Double nicuAvailableWeight = 10.0;          // NICU 보유
//
//
//    // 실시간 상태 가중치
//    @Builder.Default
//    private Double deliveryRoomAvailableWeight = 30.0;  // 분만실 여유
//
//    @Builder.Default
//    private Double emergencyRoomAvailableWeight = 30.0; // 응급실 가용성
//
//
//    // 구조 기준값
//    @Builder.Default
//    private Integer operatingRoomThreshold = 3;         // 수술실 기준 개수
//
//    // 장비 / 추가 가중치
//    @Builder.Default
//    private Double incubatorWeight = 10.0;              // 인큐베이터 가점
//
//    @Builder.Default
//    private Double prematureVentilatorWeight = 5.0;     // 조산아 호흡기 가점
//
//    @Builder.Default
//    private Double operatingRoomBonusWeight = 5.0;      // 수술실 보너스
//
//    @Builder.Default
//    private Double nicuScaleWeight = 2.0;
//    @Builder.Default
//    private Double maxNicuScaleScore = 10.0;// NICU 병상 1개당 점수

//    public void updatePregnantWeights(
//            Double delivery,
//            Double surgery,
//            Double nicu,
//            Double deliveryRoom,
//            Double emergencyRoom,
//            Integer operatingRoom,
//            Double incubator,
//            Double ventilator,
//            Double operatingRoomBonus,
//            Double nicuScaleWeight,
//            Double maxNicuScaleScore
//    ) {
//        this.deliveryAvailableWeight = delivery;
//        this.obstetricSurgeryWeight = surgery;
//        this.nicuAvailableWeight = nicu;
//
//        this.deliveryRoomAvailableWeight = deliveryRoom;
//        this.emergencyRoomAvailableWeight = emergencyRoom;
//
//        this.operatingRoomThreshold = operatingRoom;
//
//        this.incubatorWeight = incubator;
//        this.prematureVentilatorWeight = ventilator;
//        this.operatingRoomBonusWeight = operatingRoomBonus;
//        this.nicuScaleWeight = nicuScaleWeight;
//        this.maxNicuScaleScore = maxNicuScaleScore;
//    }
