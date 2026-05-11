package com.itcen.emergencyroad.pediatric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PediatricHospitalListDto {
    private String hpid; // 병원 아이디
    private String hospitalName; // 병원 이름
    private Integer availablePediatricBedCount; // 소아 가용 병상 수
    private Integer totalPediatricBedCount; // 소아 전체 병상 수
    private LocalDateTime recordedAt; // 입력일시

    private Double hospitalLatitude; // 병원 위도
    private Double hospitalLongitude; // 병원 경도
    private Double distanceKm;

    public void updateDistanceKm(Double distanceKm){
        this.distanceKm = distanceKm;
    }
}

