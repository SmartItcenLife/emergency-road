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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightPediatricConfiguration extends BaseEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private HospitalCategory category;

    // 기본 소아 응급 가능
    @Builder.Default
    private Double pediatricEmergencyWeight = 40.0;

    // 소아 중환자실
    @Builder.Default
    private Double pediatricIcuWeight = 20.0;

    // 응급전용 소아 ICU
    @Builder.Default
    private Double pediatricEmergencyIcuWeight = 25.0;

    // 인큐베이터
    @Builder.Default
    private Double incubatorWeight = 15.0;

    // 조산아 인공호흡기
    @Builder.Default
    private Double preemieVentiWeight = 15.0;

    // 소아 인공호흡기
    @Builder.Default
    private Double pediatricVentiWeight = 10.0;

    // 음압격리
    @Builder.Default
    private Double negativeIsolationWeight = 10.0;

    // 일반격리
    @Builder.Default
    private Double isolationWeight = 5.0;

    // 특수 응급질환 대응
    @Builder.Default
    private Double specialTreatmentWeight = 15.0;

    // 저체중 출생아
    @Builder.Default
    private Double lowBirthWeightBonus = 20.0;
}