package com.itcen.emergencyroad.general.repository;

import com.itcen.emergencyroad.general.entity.General;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneralRepository extends JpaRepository<General, Long> {
    // 뼈대가 만들어지면 기본 기능(save, findAll 등)은 자동으롬 만들어짐

    Optional<General> findByHpid(String hpid); // 병원 코드로 기존 데이터를 찾는 메서드
}
