package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface HospitalRepository extends JpaRepositoryImplementation<Hospital, String> {
    Optional<Hospital> findByHpid(String hpid);
    //여러 HPID를 한 번에 조회
    List<Hospital> findAllByHpidIn(java.util.Collection<String> hpids);
    // 병원별 커뮤니티에서 병원 이름 가져오기 위한 JPQL
    @Query("select h.hospitalName from Hospital h where h.hpid = :hpid ")
    String findHospitalByHpid(@Param("hpid") String hpid);
}
