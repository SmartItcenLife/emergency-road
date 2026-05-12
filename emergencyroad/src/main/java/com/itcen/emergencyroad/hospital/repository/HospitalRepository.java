package com.itcen.emergencyroad.hospital.repository;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.recommend.dto.projection.PregnantHospitalProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepositoryImplementation<Hospital, String> {
    Optional<Hospital> findByHpid(String hpid);
    //여러 HPID를 한 번에 조회
    List<Hospital> findAllByHpidIn(java.util.Collection<String> hpids);


    //필요한 엔티티들을 직접 조인해서 가져오는 쿼리
    @Query("""
            SELECT h as hospital,
                   hd as detail,
                   s as score,
                   p as pregnant,
                   pr as realtime,
                   ps as standard
            FROM Hospital h
            LEFT JOIN HospitalDetail hd ON hd.hospital = h
            LEFT JOIN HospitalScore s ON s.hospital = h
            LEFT JOIN Pregnant p ON p.hospital = h
            LEFT JOIN PregnantRealtime pr ON pr.hospital = h
            LEFT JOIN PregnantStandard ps ON ps.hospital = h
            """)
    List<PregnantHospitalProjection> findAllHospitalPregnantData();
}
