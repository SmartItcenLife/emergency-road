package com.itcen.emergencyroad.pregnant.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PregnantRealtimeRepository extends JpaRepository<PregnantRealtime, Long> {

    Optional<PregnantRealtime> findPregnantRealtimeByHospital(Hospital hospital);

}