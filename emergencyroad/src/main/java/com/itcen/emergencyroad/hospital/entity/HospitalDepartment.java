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
    @JoinColumn(name = "hpid")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}