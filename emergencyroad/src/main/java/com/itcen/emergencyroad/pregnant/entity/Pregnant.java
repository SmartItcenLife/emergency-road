package com.itcen.emergencyroad.pregnant.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

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

    // 분만실 수 getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hv42")
    private String  hv42;

    //인큐베이터 보육기 여부 (Y/N) getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hv11", length = 20)
    private String hv11;

    // 분만 가능 여부 (Y/N) - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty16", length = 20)
    private String mkioskty16;

    // 산과 수술 가능 여부(Y/N) -  getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty17", length = 20)
    private String mkioskty17;

    // 부인과 수술 가능 여부(Y/N) - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty18", length = 20)
    private String mkioskty18;

    // 신생아 / NICU
    // NICU 병상 수 getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hvncc")
    private Integer hvncc;

    // 저체중아 집중치료 가능 여부(Y/N) -getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty15", length = 20)
    private String mkioskty15;

    // 장비
    // 인큐베이터가용(Y/N)  - getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hvincuayn", length = 20)
    private String hvincuayn;

    // 조산아 인공호흡기 - getEmrrmRltmUsefulSckbdInfoInqire
    @Column(name = "hvventisoayn", length = 20)
    private String hvventisoayn;

    // 기타
    // 응급투석 - getSrsillDissAceptncPosblInfoInqire
    @Column(name = "mkioskty22", length = 20)
    private String mkioskty22;


}