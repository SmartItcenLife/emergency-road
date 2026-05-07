package com.itcen.emergencyroad.pregnant.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PregnantRepository extends JpaRepository<Pregnant, Long> {

    Optional<Pregnant> findByHospital(Hospital hospital);
}