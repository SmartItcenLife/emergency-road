package com.itcen.emergencyroad.pediatric.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
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
    private Double distanceKm; // 사용자 위치 기준 거리
    private Double duration; //소요 시간

    // Repository의 JPQL constructor projection은 기존 9개 인자로 생성하므로
    // duration 필드 추가 후에도 기존 쿼리 검증이 통과되도록 호환 생성자를 둔다.
    public PediatricHospitalListDto(
            String hpid,
            String hospitalName,
            Integer availablePediatricBedCount,
            Integer totalPediatricBedCount,
            LocalDateTime recordedAt,
            String emergencyPhone,
            Double hospitalLatitude,
            Double hospitalLongitude,
            Double distanceKm
    ) {
        this.hpid = hpid;
        this.hospitalName = hospitalName;
        this.availablePediatricBedCount = availablePediatricBedCount;
        this.totalPediatricBedCount = totalPediatricBedCount;
        this.recordedAt = recordedAt;
        this.emergencyPhone = emergencyPhone;
        this.hospitalLatitude = hospitalLatitude;
        this.hospitalLongitude = hospitalLongitude;
        this.distanceKm = distanceKm;
    }

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

    public void updateRouteInfo(Double distanceKm, Integer duration){
        this.distanceKm = distanceKm;
        this.duration = duration != null ? duration.doubleValue() : null;
    }
}
