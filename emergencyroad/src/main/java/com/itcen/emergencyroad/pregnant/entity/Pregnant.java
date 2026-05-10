package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

//거의 안 변하는 운영 가능 여부
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pregnant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //병원 ID (FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    // 분만 가능 여부 (Y/N) - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty16", length = 20)
    private String mkioskty16;

    // 산과 수술 가능 여부(Y/N) -  getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty17", length = 20)
    private String mkioskty17;

    // 부인과 수술 가능 여부(Y/N) - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty18", length = 20)
    private String mkioskty18;

    // 저체중아 집중치료 가능 여부(Y/N) -getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty15", length = 20)
    private String mkioskty15;

    // 기타
    // 응급투석 - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty22", length = 20)
    private String mkioskty22;

    public void update(EmrDto dto) {
        this.mkioskty15 = dto.getMKioskTy15();
        this.mkioskty16 = dto.getMKioskTy16();
        this.mkioskty17 = dto.getMKioskTy17();
        this.mkioskty18 = dto.getMKioskTy18();
        this.mkioskty22 = dto.getMKioskTy22();
    }

}