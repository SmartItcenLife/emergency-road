package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.HospitalMessage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HospitalMessageRepository extends JpaRepository<HospitalMessage,Long> {
}
