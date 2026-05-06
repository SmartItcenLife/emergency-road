package com.itcen.emergencyroad.hospital.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital_message")
@Getter
@NoArgsConstructor
public class HospitalMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "disease_name")
    private String diseaseName;

    @Column(name = "message")
    private String message;

    @Column(name = "blocked_start")
    private Integer blockedStart;

    @Column(name = "blocked_end")
    private Integer blockedEnd;
}