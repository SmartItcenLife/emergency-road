package com.itcen.emergencyroad.admin.service;

import com.itcen.emergencyroad.recommend.dto.weight.GeneralWeightRequestDto;
import com.itcen.emergencyroad.recommend.dto.weight.GeneralWeightResponseDto;
import com.itcen.emergencyroad.recommend.dto.weight.PregnantWeightRequestDto;
import com.itcen.emergencyroad.recommend.dto.weight.PregnantWeightResponseDto;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.WeightGeneralConfiguration;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import com.itcen.emergencyroad.recommend.repository.WeightGeneralConfigurationRepository;
import com.itcen.emergencyroad.recommend.repository.WeightPregnantConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminWeightService {
    private final WeightGeneralConfigurationRepository generalRepository;
    private final WeightPregnantConfigurationRepository pregnantRepository;

    // 화면에 보여줄 일반 응급 가중치 조회
    @Transactional
    public GeneralWeightResponseDto getGeneralWeight(){
        WeightGeneralConfiguration config = generalRepository.findById(HospitalCategory.GENERAL)
                .orElseGet(() -> {
                    // 데이터가 아예 없다면 기본값으로 새로 만들어서 DB에 저장해둠 (에러 방지)
                    WeightGeneralConfiguration newConfig = WeightGeneralConfiguration.builder()
                            .category(HospitalCategory.GENERAL)
                            .build();
                    return generalRepository.save(newConfig);
                });
        return new GeneralWeightResponseDto(config);
    }
    // 관리자가 입력한 일반 응급 가중치 저장
    @Transactional
    public void updateGeneralWeight(GeneralWeightRequestDto dto){
        WeightGeneralConfiguration config = generalRepository.findById(HospitalCategory.GENERAL)
                .orElseThrow(()->new IllegalArgumentException("일반 응급 가중치 설정이 존재하지 않습니다."));

        config.updateGeneralWeights(
                dto.getEmergencyRoomWeight(),
                dto.getSevereDiseaseWeight(),
                dto.getIcuWeight(),
                dto.getEquipmentWeight(),
                dto.getCongestionWeight(),
                dto.getEcmoBonus(),
                dto.getCrrtBonus(),
                dto.getAngioBonus()
        );
    }

    // 화면에 보여줄 임산부 응급 가중치 조회
    @Transactional
    public PregnantWeightResponseDto getPregnantWeight(){
        WeightPregnantConfiguration config = pregnantRepository.findById(HospitalCategory.PREGNANT)
                .orElseGet(()->{
                    // 데이터가 아예 없다면 기본값으로 새로 만들어서 DB에 저장해둠 (에러 방지)
                    WeightPregnantConfiguration newConfig = WeightPregnantConfiguration.builder()
                            .category(HospitalCategory.PREGNANT)
                            .build();
                    return pregnantRepository.save(newConfig);
                });
        return new PregnantWeightResponseDto(config);
    }
    // 관리자가 입력한 임산부 응급 가중치 저장
    @Transactional
    public void updatePregnantWeight(PregnantWeightRequestDto dto){
        WeightPregnantConfiguration config = pregnantRepository.findById(HospitalCategory.PREGNANT)
                .orElseThrow(()->new IllegalArgumentException("일반 응급 가중치 설정이 존재하지 않습니다."));

        config.updatePregnantWeights(
                dto.getDeliveryAvailableWeight(),
                dto.getObstetricSurgeryWeight(),
                dto.getNicuAvailableWeight(),
                dto.getDeliveryRoomAvailableWeight(),
                dto.getEmergencyRoomAvailableWeight(),
                dto.getOperatingRoomThreshold(),
                dto.getIncubatorWeight(),
                dto.getPrematureVentilatorWeight(),
                dto.getOperatingRoomBonusWeight(),
                dto.getNicuScaleWeight(),
                dto.getMaxNicuScaleScore()
        );
    }

}
