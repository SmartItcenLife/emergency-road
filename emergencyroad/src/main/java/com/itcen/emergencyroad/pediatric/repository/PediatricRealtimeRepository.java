package com.itcen.emergencyroad.pediatric.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
        pr.recordedAt
    )
    from PediatricRealtime pr
    join pr.hospital h
    left join PediatricStandard ps on ps.hospital = h
""")
    List<PediatricHospitalListDto> findPediatricHospitalList();

}
