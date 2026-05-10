package com.itcen.emergencyroad.pediatric.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PediatricRealtimeRepository extends JpaRepository<PediatricRealtime,Long> {
    Optional<PediatricRealtime> findByHospital(Hospital hospital);
}
