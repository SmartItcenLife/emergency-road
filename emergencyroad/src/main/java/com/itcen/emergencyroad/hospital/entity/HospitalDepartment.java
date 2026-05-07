package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital_departments")
@Getter
@NoArgsConstructor
public class HospitalDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hpid", referencedColumnName = "hpid", nullable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;
}