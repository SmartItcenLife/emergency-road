package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital_master")
@Getter
@NoArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="hospital_name", nullable = false)
    private String hospitalName; //병원이름

    @Column(name = "hospital_code", nullable = false)
    private String hospitalCode; //병원 코드

    @Column(name = "old_hospital_code", nullable = false)
    private String oldHospitalCode; //병원 구코드

    @Column(name = "address", nullable = false)
    private String address; //주소

    @Column(name = "phone")
    private String phone; //대표번호

    @Column(name = "emergency_phone")
    private String emergencyPhone; //응급실 번호

    @Column(name = "emergency_type")
    private String emergencyType;  //구분

    @Column(name = "latitude")
    private Double latitude;  //위도

    @Column(name = "longitude")
    private Double longitude; //경도

    @Column(name = "has_emergency")
    private Boolean hasEmergency; //응급실 가용여부
}