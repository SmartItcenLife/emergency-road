package com.itcen.emergencyroad.general.repository;

import com.itcen.emergencyroad.general.dto.GeneralHospitalListDto;
import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GeneralRepository extends JpaRepository<GeneralRealTimeAndStandard, Long> {
    // 뼈대가 만들어지면 기본 기능(save, findAll 등)은 자동으롬 만들어짐
   Optional<GeneralRealTimeAndStandard> findByHospital(Hospital hospital);
   //Optional<General> findByHpid(String hpid); // 병원 코드로 기존 데이터를 찾는 메서드
   @Query("""
    select new com.itcen.emergencyroad.general.dto.GeneralHospitalListDto(
        h.hpid,
        h.hospitalName,
        gr.emergencyAvailableBeds,
        gr.emergencyTotalBeds,
        h.emergencyPhone,
        h.latitude,
        h.longitude,
        null
    )
    from GeneralRealTimeAndStandard gr
    join gr.hospital h
""")
   List<GeneralHospitalListDto> findGeneralHospitalList();
}
