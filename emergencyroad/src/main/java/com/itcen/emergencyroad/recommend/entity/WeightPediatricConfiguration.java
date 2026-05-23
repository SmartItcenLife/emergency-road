package com.itcen.emergencyroad.recommend.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HospitalCategory category;

    //1. availability 수용 가능성
    @Builder.Default
    private Double availabilityWeight  = 40.0;  // 기본 소아 응급 가능

    //2. medical 의료 자원
    @Builder.Default
    private Double medicalWeight  = 35.0;// 인큐베이터


    //3. special 특수 치료 가능
    @Builder.Default
    private Double specialTreatmentWeight = 25.0; // 특수 응급질환 대응


    /*
//    // 저체중 출생아
//    @Builder.Default
//    private Double lowBirthWeightBonus = 10.0;

    //    // 소아 중환자실
//    @Builder.Default
//    private Double pediatricIcuWeight = 11.0;
//
//    // 응급전용 소아 ICU
//    @Builder.Default
//    private Double pediatricEmergencyIcuWeight = 11.0;


//    // 조산아 인공호흡기
//    @Builder.Default
//    private Double preemieVentiWeight = 6.0;
//
//    // 소아 인공호흡기
//    @Builder.Default
//    private Double pediatricVentiWeight = 6.0;
//
//    // 음압격리
//    @Builder.Default
//    private Double negativeIsolationWeight = 6.0;
//
//    // 일반격리
//    @Builder.Default
//    private Double isolationWeight = 6.0;

     */
}