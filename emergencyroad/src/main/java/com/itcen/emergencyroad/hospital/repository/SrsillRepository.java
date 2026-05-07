package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Srsill;
import com.itcen.emergencyroad.hospital.general.entity.General;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SrsillRepository extends JpaRepository<Srsill, Long> {

    Optional<Srsill> findByHpid(String hpid); // 병원 코드로 기존 데이터를 찾는 메서드
}
