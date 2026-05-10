package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

/** 거의 변하지 않는 임산부 진료 가능 여부 정보
  *  getSrsillDissAceptncPosblInfoInqire
  */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pregnant")
public class Pregnant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원 FK
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;



    // 저체중 출생아 집중치료 가능 여부 (Y/N)

    @Column(name = "mkioskty15", length = 20)
    private String nicuAvailable;

    // 분만 가능 여부 (Y/N)
    @Column(name = "mkioskty16", length = 20)
    private String deliveryAvailable;


    // 산과 수술 가능 여부 (Y/N)
    @Column(name = "mkioskty17", length = 20)
    private String obstetricSurgeryAvailable;

    // 부인과 수술 가능 여부 (Y/N)
    @Column(name = "mkioskty18", length = 20)
    private String gynecologySurgeryAvailable;


    // 응급투석 가능 여부 (Y/N)
    @Column(name = "mkioskty22", length = 20)
    private String emergencyDialysisAvailable;

    public void update(EmrDto dto) {
        this.deliveryAvailable = dto.getMKioskTy16();
        this.obstetricSurgeryAvailable = dto.getMKioskTy17();
        this.gynecologySurgeryAvailable = dto.getMKioskTy18();
        this.nicuAvailable = dto.getMKioskTy15();
        this.emergencyDialysisAvailable = dto.getMKioskTy22();
    }
}