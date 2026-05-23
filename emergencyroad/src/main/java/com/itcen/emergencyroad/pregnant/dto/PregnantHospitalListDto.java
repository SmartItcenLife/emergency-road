package com.itcen.emergencyroad.pregnant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PregnantHospitalListDto {

    // 병원 고유 ID
    private String hpid;

    // 병원명
    private String hospitalName;

    // 사용자 위치 기준 병원까지의 거리(km)
    private Double distanceKm;

    // 응급실 연락처
    private String emergencyPhone;

    // 병원 위도
    private Double hospitalLatitude;

    // 병원 경도
    private Double hospitalLongitude;

    // 분만 가능 여부
    private String deliveryAvailable;

    // 현재 분만실 가용 여부
    private String isDeliveryRoomAvailable;

    // 현재 NICU 가용 병상 수
    private Integer nicuBedCount;

    // NICU 기준 병상 수
    private Integer nicuStandard;

    // 시간
    private Double duration;

    public String getDeliveryAvailableLabel() {
        return toAvailabilityLabel(deliveryAvailable);
    }

    public String getDeliveryRoomAvailableLabel() {
        return toAvailabilityLabel(isDeliveryRoomAvailable);
    }

    public Integer getNicuAvailablePercentage() {
        if (nicuBedCount == null || nicuStandard == null) {
            return null;
        }

        if (nicuStandard == 0) {
            return null;
        }

        return (int) Math.round((nicuBedCount * 100.0) / nicuStandard);
    }

    public String getNicuCongestionLabel() {
        Integer percentage = getNicuAvailablePercentage();

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

    private String toAvailabilityLabel(String value) {
        if (value == null || value.isBlank()) {
            return "정보없음";
        }

        String normalized = value.trim();

        if ("Y".equalsIgnoreCase(normalized) || "Y1".equalsIgnoreCase(normalized)) {
            return "가능";
        }

        if ("N".equalsIgnoreCase(normalized) || "N1".equalsIgnoreCase(normalized)) {
            return "불가";
        }

        return normalized;
    }

    public void updateRouteInfo(Double distanceKm, Integer duration){
        this.distanceKm = distanceKm;
        this.duration = duration != null ? duration.doubleValue() : null;
    }
}