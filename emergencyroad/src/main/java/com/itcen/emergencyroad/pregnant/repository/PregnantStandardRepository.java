package com.itcen.emergencyroad.pregnant.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PregnantStandardRepository extends JpaRepository<PregnantStandard, Long> {
    Optional<PregnantStandard> findPregnantStandardByHospital(Hospital hospital);

}