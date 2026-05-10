package com.itcen.emergencyroad.general.repository;

import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SrsIllRepository extends JpaRepository<GeneralSrsIll, Long> {
    Optional<GeneralSrsIll> findByHospital(Hospital hospital); // 병원 코드로 기존 데이터를 찾는 메서드

}
