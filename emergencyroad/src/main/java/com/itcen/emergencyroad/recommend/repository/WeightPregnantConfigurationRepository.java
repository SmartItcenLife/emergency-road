package com.itcen.emergencyroad.recommend.repository;

import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeightPregnantConfigurationRepository extends JpaRepository<WeightPregnantConfiguration, HospitalCategory> {

    // 생성일시(CreatedAt) 기준으로 최근 것 하나 가져오기
    Optional<WeightPregnantConfiguration> findTopByCategoryOrderByCreatedAtDesc(HospitalCategory category);
}