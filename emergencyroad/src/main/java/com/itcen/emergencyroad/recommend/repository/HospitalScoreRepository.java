package com.itcen.emergencyroad.recommend.repository;

import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalScoreRepository extends JpaRepository<HospitalScore,String>  {
    Optional<HospitalScore> findByHospital_Hpid(String hpid);
    List<HospitalScore> findAllByPregnantScoreGreaterThan(Double score);
    List<HospitalScore> findAllByGeneralScoreGreaterThan(Double score);
}
