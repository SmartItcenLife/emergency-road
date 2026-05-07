package com.itcen.emergencyroad.hospital.general.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA에서 사용하는 기본키 ID

    @Column(name = "hpid")
    private String hpid; // 병원 고유 코드 (데이터 연결 기준값: hpid)

    @Column(name = "er_general_real_time_beds")
    private String erGeneralRealTimeBeds; // 응급실 일반 병상 실시간 가용 병상 수 (태그: hvec)

    @Column(name = "er_general_total_beds")
    private String erGeneralTotalBeds; // 응급실 일반 병상 전체 병상 수 (태그: hvs01)[cite: 5]

    @Column(name = "icu_general_real_time_beds")
    private String icuGeneralRealTimeBeds; // 일반 중환자실 실시간 가용 병상 수 (태그: hvicc)[cite: 5]

    @Column(name = "icu_general_total_beds")
    private String icuGeneralTotalBeds; // 일반 중환자실 전체 병상 수 (태그: hvs17)[cite: 5]

    @Column(name = "icu_neuro_real_time_beds")
    private String icuNeuroRealTimeBeds; // 신경과 중환자실 실시간 가용 병상 수 (태그: hvcc)[cite: 5]

    @Column(name = "icu_neuro_total_beds")
    private String icuNeuroTotalBeds; // 신경과 중환자실 전체 병상 수 (태그: hvs11)[cite: 5]

    @Column(name = "icu_thoracic_real_time_beds")
    private String icuThoracicRealTimeBeds; // 흉부외과 중환자실 실시간 가용 병상 수 (태그: hvccc)[cite: 5]

    @Column(name = "icu_thoracic_total_beds")
    private String icuThoracicTotalBeds; // 흉부외과 중환자실 전체 병상 수 (태그: hvs16)[cite: 5]

    @Column(name = "ct_available")
    private String ctAvailable; // CT 촬영 가능 여부 (태그: hvctayn)[cite: 5]

    @Column(name = "mri_available")
    private String mriAvailable; // MRI 촬영 가능 여부 (태그: hvmariayn)[cite: 5]

    @Column(name = "ventilator_available")
    private String ventilatorAvailable; // 인공호흡기 사용 가능 여부 (태그: hvventiayn)[cite: 5]

    @Column(name = "crrt_available")
    private String crrtAvailable; // 지속적 신대체요법, CRRT 가능 여부 (태그: hvcrrtayn)[cite: 5]

    @Column(name = "ecmo_available")
    private String ecmoAvailable; // ECMO, 체외막산소공급 장비 사용 가능 여부 (태그: hvecmoayn)[cite: 5]

    @Column(name = "angiography_available")
    private String angiographyAvailable; // 혈관조영술 가능 여부 (태그: hvangioayn)[cite: 5]

    @Column(name = "mi_available")
    private String miAvailable; // 심근경색 치료 가능 여부 (태그: mkioskty1)[cite: 5]

    @Column(name = "cerebral_infarction_available")
    private String cerebralInfarctionAvailable; // 뇌경색 치료 가능 여부 (태그: mkioskty2)[cite: 5]

    @Column(name = "sah_surgery_available")
    private String sahSurgeryAvailable; // 지주막하출혈 수술 가능 여부 (태그: mkioskty3)[cite: 5]

    @Column(name = "non_sah_surgery_available")
    private String nonSahSurgeryAvailable; // 비지주막하출혈 수술 가능 여부 (태그: mkioskty4)[cite: 5]

    @Column(name = "gi_endoscopy_available")
    private String giEndoscopyAvailable; // 위장관 내시경 가능 여부 (태그: mkioskty11)[cite: 5]

    @Column(name = "bronchoscopy_available")
    private String bronchoscopyAvailable; // 기관지 내시경 가능 여부 (태그: mkioskty13)[cite: 5]

    @Column(name = "thoracic_aorta_emergency_available")
    private String thoracicAortaEmergencyAvailable; // 흉부 대동맥 응급처치 가능 여부 (태그: mkioskty5)[cite: 5]

    @Column(name = "abdominal_aorta_emergency_available")
    private String abdominalAortaEmergencyAvailable; // 복부 대동맥 응급처치 가능 여부 (태그: mkioskty6)[cite: 5]

    @Column(name = "emergency_dialysis_crrt_available")
    private String emergencyDialysisCrrtAvailable; // 응급 투석 또는 CRRT 가능 여부 (태그: mkioskty23)[cite: 5]

    @Column(name = "psychiatric_closed_ward_available")
    private String psychiatricClosedWardAvailable; // 정신과 폐쇄병동 입원 가능 여부 (태그: mkioskty24)[cite: 5]

    @Column(name = "severe_burn_treatment_available")
    private String severeBurnTreatmentAvailable; // 중증 화상 치료 가능 여부 (태그: mkioskty19)[cite: 5]

    @Column(name = "vascular_intervention_available")
    private String vascularInterventionAvailable; // 혈관 중재 시술 가능 여부 (태그: mkioskty26)[cite: 5]
}