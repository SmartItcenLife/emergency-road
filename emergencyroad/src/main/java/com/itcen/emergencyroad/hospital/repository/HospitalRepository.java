package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface HospitalRepository extends JpaRepositoryImplementation<Hospital, Long> {
    Optional<Hospital> findByHpid(String hpid);
}
