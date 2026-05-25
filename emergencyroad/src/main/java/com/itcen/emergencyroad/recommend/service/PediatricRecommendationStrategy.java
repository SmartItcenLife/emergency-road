package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.entity.*;
import com.itcen.emergencyroad.recommend.dto.projection.PediatricHospitalProjection;
import com.itcen.emergencyroad.recommend.entity.*;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightPediatricConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PediatricRecommendationStrategy implements RecommendationStrategy {

    private final HospitalScoreRepository hospitalScoreRepository;
    private final HospitalRepository hospitalRepository;
    private final WeightPediatricConfigurationRepository weightRepository;

    private static final int MIN_AVAILABLE_COUNT = 0;

    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.PEDIATRIC;
    }

    @Override
    @Transactional
    public void calculateScores() {
        log.info("===== 소아 응급 점수 계산 시작 =====");

        WeightPediatricConfiguration config = weightRepository
                .findTopByCategoryOrderByCreatedAtDesc(HospitalCategory.PEDIATRIC)
                .orElseThrow(() -> new RuntimeException("소아 응급 가중치 설정이 없습니다."));

        List<PediatricHospitalProjection> results = hospitalRepository.findAllHospitalPediatricData();
        log.info("조회 병원 수 = {}", results.size());

        for (PediatricHospitalProjection row : results) {
            Hospital hospital = row.getHospital();

            HospitalScore scoreEntity = hospitalScoreRepository.findByHospital_Hpid(hospital.getHpid())
                    .orElse(HospitalScore.builder().hospital(hospital).build());

            calculatePediatricScore(row, scoreEntity, config);

            hospitalScoreRepository.save(scoreEntity);
        }
        log.info("===== 소아 응급 점수 계산 종료 =====");
    }

    public void calculatePediatricScore(
            PediatricHospitalProjection row,
            HospitalScore scoreEntity,
            WeightPediatricConfiguration config
    ) {
        if (row.getPediatricRealtime() == null) {
            scoreEntity.updatePediatricScore(0.0, "소아 실시간 정보 없음");
            return;
        }

        List<String> tags = new ArrayList<>();

        double availabilityScore = calculateAvailabilityScore(row, config, tags);
        double medicalScore = calculateMedicalScore(row.getPediatricRealtime(), config, tags);
        double specialScore = calculateSpecialScore(row.getPediatricMkioskty(), config, tags);

        double finalScore = Math.min( availabilityScore + medicalScore + specialScore, 100.0);

        log.debug("병원: {}, 최종 점수: {}, 태그: {}", row.getHospital().getHospitalName(), finalScore, tags);
        scoreEntity.updatePediatricScore(finalScore, String.join(" | ", tags));
    }


    private double calculateAvailabilityScore(PediatricHospitalProjection row, WeightPediatricConfiguration config, List<String> tags) {
        PediatricRealtime realtime = row.getPediatricRealtime();
        PediatricStandard standard = row.getPediatricStandard();

        double bedRatio = 0;
        if (realtime.getPediatricBedCount() != null
                && standard.getPediatricBedStandard() != null
                && standard.getPediatricBedStandard() > 0) {

            bedRatio = (double) realtime.getPediatricBedCount() / standard.getPediatricBedStandard();
            bedRatio = Math.min(bedRatio, 1.0);
            tags.add("입원가능");
        }

        // 2. ICU 점수 계산
        double icuScore = 0.0;

        if (realtime.getPediatricIcuCount() != null && realtime.getPediatricIcuCount() > MIN_AVAILABLE_COUNT) {
            icuScore += 0.5;
            tags.add("소아ICU");
        }
        if (realtime.getPediatricEmergencyIcuCount() != null && realtime.getPediatricEmergencyIcuCount() > MIN_AVAILABLE_COUNT) {
            icuScore += 0.5;
            //tags.add("응급 소아 중환자실");
        }
        double score = (bedRatio * 0.7) + (Math.min(icuScore, 1.0) * 0.3);

        return score * config.getAvailabilityWeight();

    }

    private double calculateMedicalScore(PediatricRealtime realtime, WeightPediatricConfiguration config, List<String> tags) {
        double score=0;

        if (isAvailable(realtime.getIncubatorResourceAvailable())) {
            score += config.getMedicalWeight() * 0.30;
            //tags.add("인큐베이터");
        }
        if (isAvailable(realtime.getPreemieVentiAvailable())) {
            score += config.getMedicalWeight() * 0.30;
           // tags.add("조산아호흡기");
        }
        if (isAvailable(realtime.getPediatricVentiAvailable())) {
            score += config.getMedicalWeight() * 0.20;
           // tags.add("소아호흡기");
        }
        if (realtime.getPediatricNegativeIsolationCount() != null && realtime.getPediatricNegativeIsolationCount() > MIN_AVAILABLE_COUNT) {
            score += config.getMedicalWeight() * 0.10;
            //tags.add("음압격리");
        }
        if (realtime.getPediatricGeneralIsolationCount() != null && realtime.getPediatricGeneralIsolationCount() > MIN_AVAILABLE_COUNT) {
            score += config.getMedicalWeight() * 0.10;
            //tags.add("일반격리");
        }

        return score;
    }

    private double calculateSpecialScore(PediatricMkioskty mkiosk, WeightPediatricConfiguration config, List<String> tags) {
        if (mkiosk == null) return 0.0;
        double score=0;


        if (isAvailable(mkiosk.getLowBirthWeightInfantAvailable())) {
            score += config.getSpecialTreatmentWeight() * 0.32;
            //tags.add("저체중출생아");
        }
        if (isAvailable(mkiosk.getPediatricEmergencyEndoscopyBronchialAvailable())) {
            score += config.getSpecialTreatmentWeight() * 0.24;
            //tags.add("기관지내시경");
        }
        if (isAvailable(mkiosk.getPediatricEmergencyEndoscopyGastroAvailable())) {
            score += config.getSpecialTreatmentWeight() * 0.16;
            //.add("소아위장관내시경");
        }
        if (isAvailable(mkiosk.getPediatricVascularInterventionAvailable())) {
            score += config.getSpecialTreatmentWeight() * 0.16;
            //tags.add("소아혈관중재");
        }
        if (isAvailable(mkiosk.getPediatricBowelObstructionAvailable())) {
            score += config.getSpecialTreatmentWeight() * 0.12;
            //tags.add("장중첩");
        }

        return score;
    }

    private boolean isAvailable(String value) {
        return value != null && "Y".equalsIgnoreCase(value.trim());
    }
}