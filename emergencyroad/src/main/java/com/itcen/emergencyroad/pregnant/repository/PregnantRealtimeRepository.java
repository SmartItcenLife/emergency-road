package com.itcen.emergencyroad.pregnant.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PregnantRealtimeRepository extends JpaRepository<PregnantRealtime, Long> {

    Optional<PregnantRealtime> findPregnantRealtimeByHospital(Hospital hospital);


    @Query("""
            select new com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto(
                h.hpid,
                h.address,
                h.emergencyPhone,
                h.phone,
                pr.nicuBedCount,
                pr.isDeliveryRoomAvailable,
                pr.incubatorAvailable,
                pr.prematureVentilatorAvailable,
                ps.deliveryRoomStandard,
                ps.nicuStandard,
                ps.ventilatorStandard,
                ps.incubatorStandard,
                p.nicuAvailable,
                p.deliveryAvailable,
                p.obstetricSurgeryAvailable,
                p.gynecologySurgeryAvailable,
                p.emergencyDialysisAvailable,
            
                pr.createdAt,
                ps.createdAt
            )
            from PregnantRealtime pr
            join pr.hospital h
            left join PregnantStandard ps on ps.hospital = h
            left join Pregnant p on p.hospital = h
            where h.hpid = :hpid
            """)
    Optional<PregnantHospitalDetailDto> findPregnantHospitalDetail(@Param("hpid") String hpid);
}