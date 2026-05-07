package com.itcen.emergencyroad.general.entity;

import com.itcen.emergencyroad.global.BaseEntity;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_stadard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class General extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // JPA에서 사용하는 기본키 ID

    //병원 ID (FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hpid", nullable = false)
    private Hospital hospital;

    //응급실 병상 수
    @Column(name = "hvec")
    private Integer hvec; // 응급실 일반 병상 실시간 가용 병상 수 (태그: hvec)

    @Column(name = "hvs01")
    private Integer hvs01; // 응급실 일반 병상 전체 병상 수 (태그: hvs01)[cite: 5]

    @Column(name = "hvicc")
    private Integer hvicc; // 일반 중환자실 실시간 가용 병상 수 (태그: hvicc)[cite: 5]

    @Column(name = "hvs17")
    private Integer hvs17; // 일반 중환자실 전체 병상 수 (태그: hvs17)[cite: 5]

    @Column(name = "hvcc")
    private Integer hvcc; // 신경과 중환자실 실시간 가용 병상 수 (태그: hvcc)[cite: 5]

    @Column(name = "hvs11")
    private String hvs11; // 신경과 중환자실 전체 병상 수 (태그: hvs11)[cite: 5]

    @Column(name = "hvccc")
    private Integer hvccc; // 흉부외과 중환자실 실시간 가용 병상 수 (태그: hvccc)[cite: 5]

    @Column(name = "hvs16")
    private Integer hvs16; // 흉부외과 중환자실 전체 병상 수 (태그: hvs16)[cite: 5]

    // 응급실 장비 가용 여부
    @Column(name = "hvctayn")
    private String hvctayn; // CT 촬영 가능 여부 (태그: hvctayn)[cite: 5]

    @Column(name = "hvmariayn")
    private String hvmariayn; // MRI 촬영 가능 여부 (태그: hvmariayn)[cite: 5]

    @Column(name = "hvventiayn")
    private String hvventiayn; // 인공호흡기 사용 가능 여부 (태그: hvventiayn)[cite: 5]

    @Column(name = "hvcrrtayn")
    private String hvcrrtayn; // 지속적 신대체요법, CRRT 가능 여부 (태그: hvcrrtayn)[cite: 5]

    @Column(name = "hvecmoayn")
    private String hvecmoayn; // ECMO, 체외막산소공급 장비 사용 가능 여부 (태그: hvecmoayn)[cite: 5]

    @Column(name = "hvangioayn")
    private String hvangioayn; // 혈관조영술 가능 여부 (태그: hvangioayn)[cite: 5]

}