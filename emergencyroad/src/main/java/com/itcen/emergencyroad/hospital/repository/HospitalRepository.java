package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepositoryImplementation<Hospital, String> {
    Optional<Hospital> findByHpid(String hpid);
    //여러 HPID를 한 번에 조회
    List<Hospital> findAllByHpidIn(java.util.Collection<String> hpids);
}
