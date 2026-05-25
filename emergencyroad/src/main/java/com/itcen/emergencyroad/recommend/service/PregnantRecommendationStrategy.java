package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import com.itcen.emergencyroad.recommend.dto.projection.PregnantHospitalProjection;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightPregnantConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PregnantRecommendationStrategy implements RecommendationStrategy {

    private final HospitalScoreRepository hospitalScoreRepository;
    private final WeightPregnantConfigurationRepository weightRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.PREGNANT;
    }

    @Override
    public void calculateScores() {
        WeightPregnantConfiguration config = weightRepository
                .findTopByCategoryOrderByCreatedAtDesc(HospitalCategory.PREGNANT)
                .orElseThrow(() -> new RuntimeException("임산부 가중치 없음"));

        List<PregnantHospitalProjection> results = hospitalRepository.findAllHospitalPregnantData();

        for (PregnantHospitalProjection row : results) {
            Hospital hospital = row.getHospital();
            HospitalDetail detail = row.getDetail();
            Pregnant pregnant = row.getPregnant();
            PregnantRealtime realtime = row.getRealtime();
            PregnantStandard standard = row.getStandard();

            HospitalScore scoreEntity = hospitalScoreRepository
                    .findByHospital_Hpid(hospital.getHpid())
                    .orElse(HospitalScore.builder().hospital(hospital).build());

            calculatePregnantScore(pregnant, realtime, standard, detail, scoreEntity, config);

            hospitalScoreRepository.save(scoreEntity);
        }
    }

    public void calculatePregnantScore(
            Pregnant pregnant,
            PregnantRealtime realtime,
            PregnantStandard standard,
            HospitalDetail detail,
            HospitalScore scoreEntity,
            WeightPregnantConfiguration config
    ) {
        // [필터] 응급실 만석 체크
        if (detail == null || detail.getAvailableEmergencyBedCount() == null || detail.getAvailableEmergencyBedCount() <= 0) {
            scoreEntity.updatePregnantScore(0.0, "응급실만석");
            return;
        }

        // [필터] 분만 불가 체크
        if (pregnant == null || !isAvailable(pregnant.getDeliveryAvailable())) {
            scoreEntity.updatePregnantScore(0.0, "분만불가");
            return;
        }

        List<String> tags = new ArrayList<>();

        double availability = calculateAvailability(pregnant, realtime, detail, config, tags);
        double medical = calculateMedical(pregnant, realtime, detail, config, tags);
        double special = calculateSpecial(realtime, detail, config, tags);

        double finalScore = availability + medical + special;

        // NICU 병상이 0개이면, 전체 점수에서 특정 비율을 삭감하거나 큰 폭의 페널티를 적용
        if (realtime == null || realtime.getNicuBedCount() == null || realtime.getNicuBedCount() <= 0) {
            finalScore -= 60.0; // 60점 감점 (가중치 설정에 따라 조절 가능)
            tags.add("NICU없음");
        }
        // NICU가 있더라도 인큐베이터가 'N1'이라면 조산 대응에 큰 결격 사유가 됨
        if (realtime != null && "N1".equalsIgnoreCase(realtime.getIncubatorAvailable())) {
            finalScore -= 40.0;
            tags.add("인큐베이터불가");
        }
        // 점수가 0 미만이 되지 않도록 보정
        finalScore = Math.max(0.0, finalScore);

        scoreEntity.updatePregnantScore(finalScore, String.join(" | ", tags));
    }

    private double calculateAvailability(
            Pregnant pregnant,
            PregnantRealtime realtime,
            HospitalDetail detail,
            WeightPregnantConfiguration config,
            List<String> tags
    ) {
        double score = 0;

        if (isAvailable(pregnant.getDeliveryAvailable())) {
            score += config.getDeliveryWeight();
            tags.add("분만 가능");
        }

        score += calculateAvailabilityScore(detail, config)
                * config.getEmergencyCapacityWeight();


        if (realtime != null && isAvailable(realtime.getIsDeliveryRoomAvailable())) {
            score += config.getDeliveryRoomWeight();
            tags.add("분만실");
        }

        return score;
    }

    private double calculateMedical(
            Pregnant pregnant,
            PregnantRealtime realtime,
            HospitalDetail detail,
            WeightPregnantConfiguration config,
            List<String> tags
    ) {
        double score = 0;


        if (isAvailable(pregnant.getObstetricSurgeryAvailable())) {
            score += config.getObstetricSurgeryWeight();
            tags.add("산과수술");
        }

        if (realtime != null && realtime.getNicuBedCount() != null && realtime.getNicuBedCount() > 0) {
            score += config.getNicuWeight();
            tags.add("NICU");
        }

        if (realtime != null && isAvailable(realtime.getIncubatorAvailable())) {
            score += config.getIncubatorWeight();
        }

        if (detail.getOperatingRoomCount() != null && detail.getOperatingRoomCount() >= config.getOperatingRoomThreshold()) {
            score += config.getOperatingRoomWeight();
        }

        if (realtime != null && isAvailable(realtime.getPrematureVentilatorAvailable())) {
            score += config.getVentilatorWeight();
        }

        return score;
    }

    private double calculateSpecial(
            PregnantRealtime realtime,
            HospitalDetail detail,
            WeightPregnantConfiguration config,
            List<String> tags
    ) {
        double score = 0;

        if (detail.getHpnicuCount() != null && detail.getHpnicuCount() > 0) {
            score += Math.min(detail.getHpnicuCount(), config.getMaxNicuScaleScore());
            tags.add("고위험산모대응");
        }

        return (score / config.getMaxNicuScaleScore()) * config.getHighRiskPregnantWeight();
    }

    private double calculateAvailabilityScore(HospitalDetail detail,WeightPregnantConfiguration config) {
        if (detail == null ||detail.getEmergencyBedCount() == null || detail.getEmergencyBedCount() <= 0) {
            return 0;
        }

        double ratio = (double) detail.getAvailableEmergencyBedCount() / detail.getEmergencyBedCount();

        return ratio * config.getEmergencyCapacityWeight();
    }

    private boolean isAvailable(String value) {
        return value != null && "Y".equalsIgnoreCase(value.trim());
    }
}