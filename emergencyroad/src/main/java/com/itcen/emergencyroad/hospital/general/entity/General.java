package com.itcen.emergencyroad.hospital.general.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general")
@Getter
@Setter
public class General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA에서 사용하는 기본키 ID

    @Column(name = "hpid")
    private String hpid; // 병원 고유 코드, 병원 정보와 실시간 데이터를 연결하는 기준값

    @Column(name = "er_general_real_time_beds")
    private String erGeneralRealTimeBeds; // 응급실 일반 병상 실시간 가용 병상 수

    @Column(name = "er_general_total_beds")
    private String erGeneralTotalBeds; // 응급실 일반 병상 전체 병상 수

    @Column(name = "icu_general_real_time_beds")
    private String icuGeneralRealTimeBeds; // 일반 중환자실 실시간 가용 병상 수

    @Column(name = "icu_general_total_beds")
    private String icuGeneralTotalBeds; // 일반 중환자실 전체 병상 수

    @Column(name = "icu_neuro_real_time_beds")
    private String icuNeuroRealTimeBeds; // 신경과 중환자실 실시간 가용 병상 수

    @Column(name = "icu_neuro_total_beds")
    private String icuNeuroTotalBeds; // 신경과 중환자실 전체 병상 수

    @Column(name = "icu_thoracic_real_time_beds")
    private String icuThoracicRealTimeBeds; // 흉부외과 중환자실 실시간 가용 병상 수

    @Column(name = "icu_thoracic_total_beds")
    private String icuThoracicTotalBeds; // 흉부외과 중환자실 전체 병상 수

    @Column(name = "ct_available")
    private String ctAvailable; // CT 촬영 가능 여부

    @Column(name = "mri_available")
    private String mriAvailable; // MRI 촬영 가능 여부

    @Column(name = "ventilator_available")
    private String ventilatorAvailable; // 인공호흡기 사용 가능 여부

    @Column(name = "crrt_available")
    private String crrtAvailable; // 지속적 신대체요법, CRRT 가능 여부

    @Column(name = "ecmo_available")
    private String ecmoAvailable; // ECMO, 체외막산소공급 장비 사용 가능 여부

    @Column(name = "angiography_available")
    private String angiographyAvailable; // 혈관조영술 가능 여부

    @Column(name = "mi_available")
    private String miAvailable; // 심근경색 치료 가능 여부

    @Column(name = "cerebral_infarction_available")
    private String cerebralInfarctionAvailable; // 뇌경색 치료 가능 여부

    @Column(name = "sah_surgery_available")
    private String sahSurgeryAvailable; // 지주막하출혈 수술 가능 여부

    @Column(name = "non_sah_surgery_available")
    private String nonSahSurgeryAvailable; // 비지주막하출혈 수술 가능 여부

    @Column(name = "gi_endoscopy_available")
    private String giEndoscopyAvailable; // 위장관 내시경 가능 여부

    @Column(name = "bronchoscopy_available")
    private String bronchoscopyAvailable; // 기관지 내시경 가능 여부

    @Column(name = "thoracic_aorta_emergency_available")
    private String thoracicAortaEmergencyAvailable; // 흉부 대동맥 응급처치 가능 여부

    @Column(name = "abdominal_aorta_emergency_available")
    private String abdominalAortaEmergencyAvailable; // 복부 대동맥 응급처치 가능 여부

    @Column(name = "emergency_dialysis_crrt_available")
    private String emergencyDialysisCrrtAvailable; // 응급 투석 또는 CRRT 가능 여부

    @Column(name = "psychiatric_closed_ward_available")
    private String psychiatricClosedWardAvailable; // 정신과 폐쇄병동 입원 가능 여부

    @Column(name = "severe_burn_treatment_available")
    private String severeBurnTreatmentAvailable; // 중증 화상 치료 가능 여부

    @Column(name = "vascular_intervention_available")
    private String vascularInterventionAvailable; // 혈관 중재 시술 가능 여부
}