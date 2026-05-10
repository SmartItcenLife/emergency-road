package com.itcen.emergencyroad.pediatric.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PediatricMkiosktyRepository extends JpaRepository<PediatricMkioskty,Long> {
    Optional<PediatricMkioskty> findByHospital(Hospital hospital);
}
