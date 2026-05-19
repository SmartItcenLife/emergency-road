package com.itcen.emergencyroad.general.repository;

import com.itcen.emergencyroad.general.dto.GeneralHospitalListDto;
import com.itcen.emergencyroad.general.dto.GeneralHospitalDetailDto;
import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
        null,
        gr.recordedAt
    )
    from GeneralRealTimeAndStandard gr
    join gr.hospital h
""")
   List<GeneralHospitalListDto> findGeneralHospitalList();

   @Query("""
    select new com.itcen.emergencyroad.general.dto.GeneralHospitalDetailDto(
        h.hpid,
        h.address,
        h.emergencyPhone,
        h.phone,

        gr.emergencyAvailableBeds,
        gr.emergencyTotalBeds,

        gr.icuAvailableBeds,
        gr.icuTotalBeds,
        gr.neuroIcuAvailableBeds,
        gr.neuroIcuTotalBeds,
        gr.chestIcuAvailableBeds,
        gr.chestIcuTotalBeds,

        gr.ctAvailable,
        gr.mriAvailable,
        gr.ventilatorAvailable,
        gr.crrtAvailable,
        gr.ecmoAvailable,
        gr.angioAvailable,

        gs.MKioskTy1,
        gs.MKioskTy2,
        gs.MKioskTy3,
        gs.MKioskTy4,
        gs.MKioskTy5,
        gs.MKioskTy6,
        gs.MKioskTy23,
        gs.MKioskTy24,
        gs.MKioskTy11,
        gs.MKioskTy13,
        gs.MKioskTy19,
        gs.MKioskTy26,
        
        gr.recordedAt
    )
    from GeneralRealTimeAndStandard gr
    join gr.hospital h
    left join GeneralSrsIll gs on gs.hospital = h
    where h.hpid = :hpid
""")
   /*
    * join gr.hospital h:
    *   GeneralRealTimeAndStandard는 병원 FK를 필수로 가지므로 상세 조회의 기준 병원은 inner join으로 조회합
    *
    * left join GeneralSrsIll gs:
    *   중증질환 수용 가능 정보는 병원마다 아직 동기화되지 않았거나 누락될 수 있으므로,
    *   이 데이터가 없어도 기본 병상/장비 상세 정보는 보여주기 위해 left join을 사용
    */
   Optional<GeneralHospitalDetailDto> findGeneralHospitalDetail(@Param("hpid") String hpid);
}
