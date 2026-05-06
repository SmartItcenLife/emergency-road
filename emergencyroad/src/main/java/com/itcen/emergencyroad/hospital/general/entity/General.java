package com.itcen.emergencyroad.hospital.general.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA 필수 식별자

    @Column(name = "er_general_real_time_beds")
    private String erGeneralRealTimeBeds;

    @Column(name = "er_general_total_beds")
    private String erGeneralTotalBeds;

    @Column(name = "icu_general_real_time_beds")
    private String icuGeneralRealTimeBeds;

    @Column(name = "icu_general_total_beds")
    private String icuGeneralTotalBeds;

    @Column(name = "icu_neuro_real_time_beds")
    private String icuNeuroRealTimeBeds;

    @Column(name = "icu_neuro_total_beds")
    private String icuNeuroTotalBeds;

    @Column(name = "icu_thoracic_real_time_beds")
    private String icuThoracicRealTimeBeds;

    @Column(name = "icu_thoracic_total_beds")
    private String icuThoracicTotalBeds;

    @Column(name = "ct_available")
    private String ctAvailable;

    @Column(name = "mri_available")
    private String mriAvailable;

    @Column(name = "ventilator_available")
    private String ventilatorAvailable;

    @Column(name = "crrt_available")
    private String crrtAvailable;

    @Column(name = "ecmo_available")
    private String ecmoAvailable;

    @Column(name = "angiography_available")
    private String angiographyAvailable;

    @Column(name = "mi_available")
    private String miAvailable;

    @Column(name = "cerebral_infarction_available")
    private String cerebralInfarctionAvailable;

    @Column(name = "sah_surgery_available")
    private String sahSurgeryAvailable;

    @Column(name = "non_sah_surgery_available")
    private String nonSahSurgeryAvailable;

    @Column(name = "gi_endoscopy_available")
    private String giEndoscopyAvailable;

    @Column(name = "bronchoscopy_available")
    private String bronchoscopyAvailable;

    @Column(name = "thoracic_aorta_emergency_available")
    private String thoracicAortaEmergencyAvailable;

    @Column(name = "abdominal_aorta_emergency_available")
    private String abdominalAortaEmergencyAvailable;

    @Column(name = "emergency_dialysis_crrt_available")
    private String emergencyDialysisCrrtAvailable;

    @Column(name = "psychiatric_closed_ward_available")
    private String psychiatricClosedWardAvailable;

    @Column(name = "severe_burn_treatment_available")
    private String severeBurnTreatmentAvailable;

    @Column(name = "vascular_intervention_available")
    private String vascularInterventionAvailable;
}