package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HospitalDetailRepository extends JpaRepository<HospitalDetail,Long> {
    Optional<HospitalDetail> findByHospital(Hospital hospital);
    //여러 마스터에 연결된 상세 정보를 한 번에 조회
    List<HospitalDetail> findAllByHospitalIn(Collection<Hospital> hospitals);
}
