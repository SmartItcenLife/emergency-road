package com.itcen.emergencyroad.recommend.repository;

import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.WeightGeneralConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WeightGeneralConfigurationRepository  extends JpaRepository<WeightGeneralConfiguration, HospitalCategory> {
    // 생성일시(CreatedAt) 기준으로 최근 것 하나 가져오기
    Optional<WeightGeneralConfiguration> findTopByCategoryOrderByCreatedAtDesc(HospitalCategory category);

}
