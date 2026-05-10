package com.itcen.emergencyroad.hospital.entity;

import com.itcen.emergencyroad.external.dto.EgytBassDto;
import com.itcen.emergencyroad.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 병원 실시간 상세 정보 및 추천 점수 Entity
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hospital_detail")
public class HospitalDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", unique = true)
    private Hospital hospital;

    // --- 가중치 기반 추천을 위한 점수 필드 ---

    @Builder.Default
    @Column(name = "base_score")
    private Double baseScore = 0.0; // 위치(거리)를 제외한 종합 점수

    @Builder.Default
    @Column(name = "is_available_now")
    private Boolean isAvailableNow = false;// 현재 진료 가능 여부 (반정규화)

    // --- 병상 및 의료 정보 ---

    @Column(name = "dgid_id_name", columnDefinition = "TEXT")
    private String departmentNames; // 진료과목 명칭들

    @Column(name = "duty_eryn")
    private Boolean hasEmergencyRoom; // 응급실 운영 여부

    @Column(name = "hperyn")
    private Integer emergencyBedCount; // 응급실 기준 병상 수(중요 - 일반 + 임산부)

    @Column(name = "hvec")
    private Integer availableEmergencyBedCount; // 응급실 실시간 가용 병상 수

    @Column(name = "hpicuyn")
    private Integer hpicuCount; // 일반 중환자실 병상 수

    @Column(name = "hpnicuyn")
    private Integer hpnicuCount; // 신생아 중환자실 병상 수

    @Column(name = "hpopyn")
    private Integer operatingRoomCount; //수수실 수

    // --- 진료 시간 정보 ---
    @Column(name = "duty_time1s")
    private String mondayStartTime;
    @Column(name = "duty_time1c")
    private String mondayEndTime;

    @Column(name = "duty_time2s")
    private String tuesdayStartTime;
    @Column(name = "duty_time2c")
    private String tuesdayEndTime;

    @Column(name = "duty_time3s")
    private String wednesdayStartTime;
    @Column(name = "duty_time3c")
    private String wednesdayEndTime;

    @Column(name = "duty_time4s")
    private String thursdayStartTime;
    @Column(name = "duty_time4c")
    private String thursdayEndTime;

    @Column(name = "duty_time5s")
    private String fridayStartTime;
    @Column(name = "duty_time5c")
    private String fridayEndTime;

    @Column(name = "duty_time6s")
    private String saturdayStartTime;
    @Column(name = "duty_time6c")
    private String saturdayEndTime;

    @Column(name = "duty_time7s")
    private String sundayStartTime;
    @Column(name = "duty_time7c")
    private String sundayEndTime;

    // 스케줄러가 점수와 병상 수 업데이트할 때 사용 메서드
    public void updateRealtimeStatus(
            Integer availableEmergencyBedCount,
            Double baseScore,
            Boolean isAvailableNow
    ) {
        this.availableEmergencyBedCount = availableEmergencyBedCount;
        this.baseScore = baseScore;
        this.isAvailableNow = isAvailableNow;
    }

    // 5번 API - 더티 체크를 위한 업데이트 메서드
    public void updateDetail(EgytBassDto dto) {

        this.departmentNames = dto.getDgidIdName();
        this.hasEmergencyRoom = dto.getDutyEryn();

        this.operatingRoomCount = dto.getHpopyn();
        this.emergencyBedCount = dto.getHperyn();
        this.availableEmergencyBedCount = dto.getHvec();

        this.hpicuCount = dto.getHpicuyn();

        this.hpnicuCount = dto.getHpnicuyn();
        this.mondayStartTime = dto.getDutyTime1s();
        this.mondayEndTime = dto.getDutyTime1c();

        this.tuesdayStartTime = dto.getDutyTime2s();
        this.tuesdayEndTime = dto.getDutyTime2c();

        this.wednesdayStartTime = dto.getDutyTime3s();
        this.wednesdayEndTime = dto.getDutyTime3c();

        this.thursdayStartTime = dto.getDutyTime4s();
        this.thursdayEndTime = dto.getDutyTime4c();

        this.fridayStartTime = dto.getDutyTime5s();
        this.fridayEndTime = dto.getDutyTime5c();

        this.saturdayStartTime = dto.getDutyTime6s();
        this.saturdayEndTime = dto.getDutyTime6c();

        this.sundayStartTime = dto.getDutyTime7s();
        this.sundayEndTime = dto.getDutyTime7c();
    }
}

