package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital_message")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "hospital_id")
    @Column(name="hpid", nullable = false, length=20)
    private String hpid; // 병원 코드

    @Column(name = "disease_code", length = 20)
    private String diseaseCode; // symTypCod - 중증질환구분

    @Column(name = "disease_name")
    private String diseaseName; // symTypCodMag

    @Column(name = "message_type", length = 400)
    private String messageType; // symBlkMsgTyp - 메시지구분

    @Column(name = "message")
    private String message; // symBlkMsg - 전달메시지

    @Column(name = "blocked_start")
    private String blockedStart; // symBlkSttDtm - 차단시작

    @Column(name = "blocked_end")
    private String blockedEnd; // symBlkEndDtm - 차단종료
}