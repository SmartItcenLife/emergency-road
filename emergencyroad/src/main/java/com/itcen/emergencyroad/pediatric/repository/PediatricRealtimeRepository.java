package com.itcen.emergencyroad.pediatric.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PediatricRealtimeRepository extends JpaRepository<PediatricRealtime,Long> {
    Optional<PediatricRealtime> findByHospital(Hospital hospital);

    @Query("""
    select new com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto(
        h.hpid,
        h.hospitalName,
        pr.pediatricBedCount,
        ps.pediatricBedStandard,
        pr.recordedAt,
        h.emergencyPhone,
        h.latitude,
        h.longitude,
        null
    )
    from PediatricRealtime pr
    join pr.hospital h
    left join PediatricStandard ps on ps.hospital = h
""")
    List<PediatricHospitalListDto> findPediatricHospitalList();

    @Query("""
    select new com.itcen.emergencyroad.pediatric.dto.PediatricHospitalDetailDto(
        h.hpid,
        h.address,
        h.emergencyPhone,
        h.phone,

        pr.pediatricIcuCount,
        pr.pediatricEmergencyIcuCount,
        pr.pediatricEmergencyAdmissionCount,
        pr.pediatricNegativeIsolationCount,
        pr.pediatricGeneralIsolationCount,

        ps.pediatricIcuStandard,
        ps.pediatricEmergencyIcuStandard,
        ps.pediatricEmergencyAdmissionStandard,
        ps.pediatricNegativeIsolationStandard,
        ps.pediatricGeneralIsolationStandard,

        pr.pediatricVentiAvailable,
        pr.preemieVentiAvailable,
        pr.incubatorAvailable,
        pr.incubatorResourceAvailable,
        pr.pediatricHotline,

        pm.pediatricBowelObstructionAvailable,
        pm.pediatricEmergencyEndoscopyGastroAvailable,
        pm.pediatricEmergencyEndoscopyBronchialAvailable,
        pm.lowBirthWeightInfantAvailable,
        pm.pediatricVascularInterventionAvailable,

        pm.pediatricBowelObstructionMessage,
        pm.pediatricEmergencyEndoscopyGastroMessage,
        pm.pediatricEmergencyEndoscopyBronchialMessage,
        pm.lowBirthWeightInfantMessage,
        pm.pediatricVascularInterventionMessage,

        pr.recordedAt,
        ps.recordedAt
    )
    from PediatricRealtime pr
    join pr.hospital h
    left join PediatricStandard ps on ps.hospital = h
    left join PediatricMkioskty pm on pm.hospital = h
    where h.hpid = :hpid
""")
    Optional<PediatricHospitalDetailDto> findPediatricHospitalDetail(@Param("hpid") String hpid);
}
