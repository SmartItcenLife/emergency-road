package com.itcen.emergencyroad.hospital.entity;

import com.itcen.emergencyroad.external.dto.EgytBassDto;
import com.itcen.emergencyroad.external.dto.EmrDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Hospital {

    // 공공데이터 기준 병원 고유 ID (비즈니스 키, 유니크)
    @Id
    @Column(name="hpid" ,unique = true, nullable = false, length = 20)
    private String hpid;

    // 병원 이름 (API에서 제공, 길이 변동 큼 → 넉넉하게 설정)
    @Column(name = "hospital_name", length = 200)
    private String hospitalName;

    // 구 병원 코드 (레거시 코드)
    @Column(name = "old_hospital_code", length = 50)
    private String oldHospitalCode;

    // 주소 (가장 긴 문자열 중 하나 → 여유 있게 설정)
    @Column(name = "address", length = 300)
    private String address;

    // 대표 전화번호
    @Column(name = "phone", length = 30)
    private String phone;

    // 응급실 전화번호
    @Column(name = "emergency_phone", length = 30)
    private String emergencyPhone;

    // 위도 (좌표 데이터)
    @Column(name = "latitude")
    private Double latitude;

    // 경도 (좌표 데이터)
    @Column(name = "longitude")
    private Double longitude;

    // 응급실 운영 여부 (Y/N or true/false 매핑)
    @Column(name = "has_emergency")
    private Boolean hasEmergency;

    // 1번 API - 더티 체크를 위한 업데이트 메서드
    public void updateMaster(EmrDto emrDto) {
        this.hospitalName = emrDto.getDutyName();
        this.emergencyPhone = emrDto.getDutyTel3(); // 응급실 전화번호
    }
    // 5번 API - 더티 체크를 위한 업데이트 메서드
    public void updateMasterInfo(EgytBassDto dto) {
        this.address = dto.getDutyAddr();
        this.phone = dto.getDutyTel1();
        this.emergencyPhone = dto.getDutyTel3();
        this.latitude = dto.getWgs84Lat();
        this.longitude = dto.getWgs84Lon();
        this.hasEmergency = dto.getDutyEryn();
    }
}