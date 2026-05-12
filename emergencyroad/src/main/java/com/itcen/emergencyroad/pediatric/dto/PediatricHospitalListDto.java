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
    private String emergencyPhone; // 응급실 병원 전화 번호

    private Double hospitalLatitude; // 병원 위도
    private Double hospitalLongitude; // 병원 경도
    private Double distanceKm;

    public void updateDistanceKm(Double distanceKm){
        this.distanceKm = distanceKm;
    }
    // TODO 일반 - 임산부 - 소아 및 유아   모두 사용할 수 있는 메서드들은 공통 유틸로 정의하여 따로 정의
    // 가용 병상 수 퍼센테이지 변환 메서드
    public Integer getAvailableBedPercentage() {
        if(availablePediatricBedCount == null || totalPediatricBedCount == null){
            return null;
        }
        // 분모가 0인경우
        if (totalPediatricBedCount == 0) {
            return null;
        }
        return (int)Math.round((availablePediatricBedCount * 100.0) / totalPediatricBedCount);
    }

    // 병원 혼잡도 임의 지정
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

