package com.itcen.emergencyroad.hospital.pediatricpatient.entity;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pediatric_mkioskty")
@Getter
@NoArgsConstructor
public class PediatricMkioskty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @Column(name = "mkioskty10")
    private Boolean mkioskty10; // 장중첩/폐색 영유아

    @Column(name = "mkioskty12")
    private Boolean mkioskty12; // 응급내시경 영유아 위장관

    @Column(name = "mkioskty14")
    private Boolean mkioskty14; // 응급내시경 영유아 기관지

    @Column(name = "mkioskty15")
    private Boolean mkioskty15; // 저체중출생아

    @Column(name = "mkioskty27")
    private Boolean mkioskty27; // 영상의학혈관중재 영유아

    @Column(name = "mkioskty10_msg", length = 255)
    private String mkioskty10Msg;

    @Column(name = "mkioskty12_msg", length = 255)
    private String mkioskty12Msg;

    @Column(name = "mkioskty14_msg", length = 255)
    private String mkioskty14Msg;

    @Column(name = "mkioskty15_msg", length = 255)
    private String mkioskty15Msg;

    @Column(name = "mkioskty27_msg", length = 255)
    private String mkioskty27Msg;
}