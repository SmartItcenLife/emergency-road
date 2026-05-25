package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.general.entity.*;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.recommend.dto.projection.GeneralHospitalProjection;
import com.itcen.emergencyroad.recommend.entity.*;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightGeneralConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneralRecommendationStrategy implements RecommendationStrategy {

    private final HospitalScoreRepository hospitalScoreRepository;
    private final HospitalRepository hospitalRepository;
    private final WeightGeneralConfigurationRepository weightRepository;

    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.GENERAL;
    }

    @Override
    @Transactional
    public void calculateScores() {
        log.info("===== 일반 응급 점수 계산 시작 =====");

        WeightGeneralConfiguration config = weightRepository
                .findTopByCategoryOrderByCreatedAtDesc(HospitalCategory.GENERAL)
                .orElseThrow(() -> new RuntimeException("일반 응급 가중치 설정이 없습니다."));

        List<GeneralHospitalProjection> results = hospitalRepository.findAllGeneralHospitalData();
        log.info("조회된 병원 수: {}", results.size());

        for (GeneralHospitalProjection row : results) {
            Hospital hospital = row.getHospital();
            HospitalScore scoreEntity = hospitalScoreRepository.findByHospital_Hpid(hospital.getHpid())
                    .orElse(HospitalScore.builder().hospital(hospital).build());

            calculateGeneralScore(row, scoreEntity, config);
            hospitalScoreRepository.save(scoreEntity);
        }
        log.info("===== 일반 응급 점수 계산 종료 =====");
    }

    public void calculateGeneralScore(
            GeneralHospitalProjection row,
            HospitalScore scoreEntity,
            WeightGeneralConfiguration config
    ) {
        GeneralRealTimeAndStandard realtime = row.getGeneralRealTimeAndStandard();

        // 필터링: 가용 병상 없음
        if (realtime == null || realtime.getEmergencyAvailableBeds() == null || realtime.getEmergencyAvailableBeds() <= 0) {
            scoreEntity.updateGeneralScore(0.0, "응급실 만석");
            return;
        }

        Set<String> tags = new LinkedHashSet<>();
        tags.add("응급실가능");
        double medicalScore = 0;

        //가용 병상 비율 반영
        if (realtime.getEmergencyTotalBeds() != null
                && realtime.getEmergencyTotalBeds() > 0) {
            double erRatio = (double) realtime.getEmergencyAvailableBeds() / realtime.getEmergencyTotalBeds();
            medicalScore += erRatio * config.getEmergencyRoomWeight();
        }

        medicalScore += calculateSevereScore(row.getGeneral(), config, tags);
        medicalScore += calculateIcuScore(realtime, config);
        medicalScore += calculateEquipmentScore(realtime, config, tags);

        double availabilityScore = calculateAvailabilityScore(realtime, config);

        double finalScore = Math.min(medicalScore + availabilityScore, 100);
        scoreEntity.updateGeneralScore(finalScore, String.join(" | ", tags));
    }

    private double calculateSevereScore(GeneralSrsIll severe, WeightGeneralConfiguration config, Set<String> tags) {
        if (severe == null) return 0.0;

        double score = 0.0;
        double maxScore = 32.0; // 총 중요도 합 (8+5+7+8+4)
        if (isAvailable(severe.getMKioskTy1())) {
            score+=8;
            tags.add("뇌출혈");
        }
        if (isAvailable(severe.getMKioskTy2())) {
            score += 5;
            tags.add("뇌경색");
        }
        if (isAvailable(severe.getMKioskTy3())) {
            score+=7;
            tags.add("심근경색");
        }
        if (isAvailable(severe.getMKioskTy5())) {
            score+=8;
            tags.add("화상");
        }
        if (isAvailable(severe.getMKioskTy23())) {
            score += 4;
            tags.add("응급투석");
        }
        return (score / maxScore) * config.getSevereDiseaseWeight();
    }

    private double calculateIcuScore(GeneralRealTimeAndStandard realtime, WeightGeneralConfiguration config) {
        double score = 0;
        double maxScore = 10;
        if(realtime.getIcuAvailableBeds() != null && realtime.getIcuAvailableBeds() >0){
            score+=2;
        }
        if(realtime.getNeuroIcuAvailableBeds() != null && realtime.getNeuroIcuAvailableBeds()>0){
            score+=4;
        }
        if(realtime.getChestIcuAvailableBeds() != null && realtime.getChestIcuAvailableBeds()>0){
            score+=4;
        }
        return (score/maxScore) * config.getIcuWeight();
    }

    private double calculateEquipmentScore(GeneralRealTimeAndStandard realtime, WeightGeneralConfiguration config, Set<String> tags) {

        double score = 0.0;
        double maxScore = 25.0; // 2+3+10+7+3
        if (isAvailable(realtime.getCtAvailable())) {
            score += 2;
            tags.add("CT");
        }
        if (isAvailable(realtime.getMriAvailable())) {
            score += 3;
            tags.add("MRI");
        }
        if (isAvailable(realtime.getEcmoAvailable())) {
            score += 10;
            tags.add("ECMO");
        }
        if (isAvailable(realtime.getCrrtAvailable())) {
            score += 7;
            //tags.add("CRRT");
        }
        if (isAvailable(realtime.getAngioAvailable())) {
            score += 3;
            //tags.add("혈관조영");
        }
        return (score / maxScore) * config.getEquipmentWeight();
    }

    private double calculateAvailabilityScore(GeneralRealTimeAndStandard realtime, WeightGeneralConfiguration config) {
        if (realtime.getEmergencyTotalBeds() == null || realtime.getEmergencyTotalBeds() <= 0) return 0.0;
        double ratio = (double) realtime.getEmergencyAvailableBeds() / realtime.getEmergencyTotalBeds();

        // 0~1 → 0~25점 //병상이 많을수록 점수 증가
        return ratio *
                config.getCongestionWeight();
    }

    private boolean isAvailable(String value) {
        return value != null && "Y".equalsIgnoreCase(value.trim());
    }
}