package com.itcen.emergencyroad.recommend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class PediatricHospitalResponseDto extends HospitalResponseDto {

    private Integer availablePediatricBedCount;
    private String congestionLabel; //혼잡도
    private Double availableBedPercentage; //퍼센트
    private String incubatorAvailable;
    private String emergencyPhone;
    private Integer totalPediatricBedCount; //전체 소아병상수
    //private Integer newbornIcuCount;            // 신생아 중환자실 가용
            // 인큐베이터 가용
   // private Boolean hasNegativeIsolation;       // 음압격리실 보유 여부
    private Double hospitalLatitude; //병원 위도
   private Double hospitalLongitude; //병원 경도

}
