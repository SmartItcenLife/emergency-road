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
public class WeightGeneralConfiguration extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HospitalCategory category;

    //1. 의료 적합도 : 응급실, 중증질환, ICU, 장비
    // 응급실
    @Builder.Default
    private Double emergencyRoomWeight = 15.0;

    @Builder.Default
    private Double severeDiseaseWeight = 25.0;// 중증질환

    @Builder.Default
    private Double icuWeight = 20.0;  // ICU

    @Builder.Default
    private Double equipmentWeight = 15.0; // 장비

    //2. 응급 수용 가능성
    @Builder.Default
    private Double congestionWeight = 25.0;  // 응급실 혼잡도

    /*
//    // 보정
//    @Builder.Default
//    private Double ecmoBonus = 10.0;
//
//    @Builder.Default
//    private Double crrtBonus = 5.0;
//
//    @Builder.Default
//    private Double angioBonus = 5.0;

    public void updateGeneralWeights(
            Double erRoom,
            Double sevDisease,
            Double icu,
            Double equipment,
            Double congestion,
            Double ecmo,
            Double crrt,
            Double angio
    ){
        this.emergencyRoomWeight = erRoom;
        this.severeDiseaseWeight = sevDisease;
        this.icuWeight = icu;
        this.equipmentWeight = equipment;
        this.congestionWeight = congestion;
//        this.ecmoBonus = ecmo;
//        this.crrtBonus = crrt;
//        this.angioBonus = angio;
    }

     */
}