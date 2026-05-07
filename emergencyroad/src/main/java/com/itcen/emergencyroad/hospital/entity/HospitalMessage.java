package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원 FK
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hpid", referencedColumnName = "hpid",nullable = false)
//    private Hospital hospital;
    @Column(name="hpid", nullable = false, length=20)
    private String hpid;

    @Column(name="duty_name", length = 30)
    private String dutyName; // symTypCod - 병원이름

    @Column(name = "disease_code", length = 20)
    private String diseaseCode; // symTypCod - 중증질환구분

    @Column(name = "disease_name", length = 100)
    private String diseaseName; // symTypCodMag - 중증질환명

    @Column(name = "message_type", length = 50)
    private String messageType; // symBlkMsgTyp - 메시지구분

    @Column(name = "message", length = 500)
    private String message; // symBlkMsg - 전달메시지

    @Column(name = "blocked_start", length = 20)
    private String blockedStart; // symBlkSttDtm - 차단시작

    @Column(name = "blocked_end", length = 20)
    private String blockedEnd; // symBlkEndDtm - 차단종료

    @Column(name = "display_yn", length = 20)
    private String displayYn; // symOutDspYon - 중증질환 표출구분

    @Column(name = "display_method", length = 20)
    private String displayMethod; // symOutDspMth - 표출 차단 구분
}