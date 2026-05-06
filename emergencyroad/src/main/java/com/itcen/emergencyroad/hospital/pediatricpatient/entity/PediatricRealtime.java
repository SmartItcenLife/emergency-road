package com.itcen.emergencyroad.hospital.pediatricpatient.entity;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pediatric_realtime")
@Getter
@NoArgsConstructor
public class PediatricRealtime {

    @Id
    private Long hospitalId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "neonatal_icu")
    private Integer neonatalIcu;

    @Column(name = "ventilator_for_premature")
    private Boolean ventilatorForPremature;

    @Column(name = "incubator_available")
    private Boolean incubatorAvailable;

    @Column(name = "pediatric_beds")
    private Integer pediatricBeds;

    @Column(name = "pediatric_icu")
    private Integer pediatricIcu;
}