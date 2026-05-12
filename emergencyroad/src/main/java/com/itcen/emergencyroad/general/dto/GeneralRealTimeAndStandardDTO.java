package com.itcen.emergencyroad.general.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GeneralRealTimeAndStandardDTO {
    // 병원 식별을 위한 고유 ID (Hospital 엔티티와 매핑용)
    private String hpid;

    // --- 응급실 병상 정보 ---
    private Integer hvec; // 응급실 일반 병상 실시간 가용 병상 수
    private Integer hvs01; // 응급실 일반 병상 전체 병상 수

    // --- 중환자실 병상 정보 ---
    private Integer hvicc; // 일반 중환자실 실시간 가용 병상 수
    private Integer hvs17; // 일반 중환자실 전체 병상 수
    private Integer hvcc; // 신경과 중환자실 실시간 가용 병상 수
    private String hvs11; // 신경과 중환자실 전체 병상 수 (엔티티 타입 유지)
    private Integer hvccc; // 흉부외과 중환자실 실시간 가용 병상 수
    private Integer hvs16; // 흉부외과 중환자실 전체 병상 수

    // --- 응급실 장비 가용 여부 ---
    private String hvctayn; // CT 촬영 가능 여부
    private String hvmariayn; // MRI 촬영 가능 여부
    private String hvventiayn; // 인공호흡기 사용 가능 여부
    private String hvcrrtayn; // 지속적 신대체요법(CRRT) 가능 여부
    private String hvecmoayn; // ECMO 장비 사용 가능 여부
    private String hvangioayn; // 혈관조영술 가능 여부
}
