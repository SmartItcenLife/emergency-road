package com.itcen.emergencyroad.general.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 일반 유형 병원 목록 전용 DTO
@Getter
@Builder
@AllArgsConstructor
public class GeneralHospitalListDto {
    private String hpid; // 병원 아이디

    private String hospitalName; // 병원 이름

    private Integer availableEmergencyBedCount; // 응급실 일반 가용 병상 수, hvec

    private Integer totalEmergencyBedCount; // 응급실 일반 전체 병상 수, hvs01

    private String emergencyPhone; // 응급실 전화번호

    private Double hospitalLatitude; // 병원 위도

    private Double hospitalLongitude; // 병원 경도

    private Double distanceKm; // 사용자 위치 기준 거리

    public void updateDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getAvailableBedPercentage() {
        if (availableEmergencyBedCount == null || totalEmergencyBedCount == null) {
            return null;
        }

        if (totalEmergencyBedCount == 0) {
            return null;
        }

        return (int) Math.round((availableEmergencyBedCount * 100.0) / totalEmergencyBedCount);
    }

    public String getCongestionLabel() {
        Integer percentage = getAvailableBedPercentage();

        if (percentage == null) {
            return "정보없음";
        }

        if (percentage >= 50) {
            return "여유";
        }

        if (percentage >= 20) {
            return "보통";
        }

        return "혼잡";
    }
}