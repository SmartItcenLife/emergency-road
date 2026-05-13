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

import java.util.List;

/**
 *
 * 임산부 추천 전략 (Pregnant Recommendation Strategy)
 * <p>
 * - 병원 + 임산부 + 실시간 데이터 기반 점수 산정
 * - 필터 → 인프라 → 대응능력 → 실시간 상태 → 혼잡도 → 규모 순으로 평가
 * - WeightPregnantConfiguration 기반으로 운영자 가중치 적용
 * - static ratio는 알고리즘 보정 계수
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PregnantRecommendationStrategy implements RecommendationStrategy {

    private final HospitalScoreRepository hospitalScoreRepository;
    private final WeightPregnantConfigurationRepository weightRepository;
    private final HospitalRepository hospitalRepository;

    // 알고리즘 보정 계수
    // 실시간 데이터는 신뢰도가 낮거나 변동이 크니까 가중치를 줄이는 용도
    private static final double DELIVERY_ROOM_WEIGHT_RATIO = 0.6;
    private static final double EMERGENCY_ROOM_WEIGHT_RATIO = 0.4;

    private static final double NORMAL_URGENCY_RATIO = 0.5;
    private static final double LOW_URGENCY_RATIO = 0.2;


    /**
     *
     * 전체 병원 데이터 기반 점수 계산 진입점
     * <p>
     * - Projection 결과(List<PregnantHospitalProjection>) 순회
     * - 병원별 점수 엔티티 생성/조회
     * - calculatePregnantScore() 호출로 실제 점수 계산 수행
     * - 결과를 DB에 저장
     */

    @Override
    public void calculateScores() {

        // 1. 임산부 가중치 조회
        WeightPregnantConfiguration config =
                weightRepository
                        .findTopByCategoryOrderByCreatedAtDesc(
                                HospitalCategory.PREGNANT
                        )
                        .orElseThrow(() ->
                                new RuntimeException("임산부 가중치 설정이 없습니다.")
                        );
        // 2. 임산부 병원 데이터 조회
        List<PregnantHospitalProjection> results =
                hospitalRepository.findAllHospitalPregnantData();

        for (PregnantHospitalProjection row : results) {

            Hospital hospital = row.getHospital();
            HospitalDetail detail = row.getDetail();
            Pregnant pregnant = row.getPregnant();
            PregnantRealtime realtime = row.getRealtime();
            PregnantStandard standard = row.getStandard();

            HospitalScore scoreEntity =
                    hospitalScoreRepository.findByHospital_Hpid(hospital.getHpid())
                            .orElse(null);

            if (scoreEntity == null) {
                scoreEntity = HospitalScore.builder()
                        .hospital(hospital)
                        .build();
            }

            calculatePregnantScore(
                    pregnant,
                    realtime,
                    standard,
                    detail,
                    scoreEntity,
                    config
            );

            hospitalScoreRepository.save(scoreEntity);
        }
    }

    /**
     * 임산부 추천 점수 핵심 로직
     * 구조:
     * 1. FILTER (탈락 조건)
     * 2. 기본 인프라 점수
     * 3. 의료 대응 능력
     * 4. 실시간 분만실 상태
     * 5. 장비 상태
     * 6. 응급실 혼잡도 반영
     * 7. NICU 규모 가점
     * <p>
     * 특징:
     * - config: 운영자 조정 가능한 가중치
     * - static ratio: 알고리즘 보정 계수
     */
    public void calculatePregnantScore(Pregnant pregnant,
                                       PregnantRealtime realtime,
                                       PregnantStandard standard,
                                       HospitalDetail detail,
                                       HospitalScore scoreEntity,
                                       WeightPregnantConfiguration config) {

        double score = 0.0;
        StringBuilder tags = new StringBuilder();

        // 1. FILTER :  응급실 또는 분만 불가 병원은 즉시 제외
        if (detail == null
                || detail.getAvailableEmergencyBedCount() == null
                || detail.getAvailableEmergencyBedCount() <= 0) {

            scoreEntity.updatePregnantScore(0.0, "응급실 만석 주의");
            return;
        }

        if (pregnant == null
                || !isAvailable(pregnant.getDeliveryAvailable())) {

            scoreEntity.updatePregnantScore(0.0, "분만 불가");
            return;
        }

        // 2. 기본 인프라 : 분만 가능 여부 + 수술실 규모 기반 기본 점수
        score += config.getDeliveryAvailableWeight();
        tags.append("분만가능");

        if (detail.getOperatingRoomCount() != null
                && detail.getOperatingRoomCount() >= config.getOperatingRoomThreshold()) {

            score += config.getOperatingRoomBonusWeight();
            tags.append(" | 수술실다수");
        }

        // 3. 대응 능력 : 산과 수술 / NICU 보유 여부 기반 추가 점수
        if (isAvailable(pregnant.getObstetricSurgeryAvailable())) {
            score += config.getObstetricSurgeryWeight();
            tags.append(" | 산과수술");
        }

        if (isAvailable(pregnant.getNicuAvailable())) {
            score += config.getNicuAvailableWeight();
            tags.append(" | NICU보유");
        }

        // 4. 분만실 : 실시간 분만실 가용성 반영 (신뢰도 낮아 60%만 반영)
        if (realtime != null
                && isAvailable(realtime.getIsDeliveryRoomAvailable())) {

            //분만실 여유 점수를 100% 다 주지 않고 60%만 반영
            score += config.getDeliveryRoomAvailableWeight()
                    * DELIVERY_ROOM_WEIGHT_RATIO;

            tags.append(" | 분만실여유");
        } else {
            tags.append(" | 분만실혼잡");
        }

        // 5. 장비 상태 : 인큐베이터 / 조산아 호흡기 여부 추가 점수
        if (realtime != null) {

            if (isAvailable(realtime.getIncubatorAvailable())) {
                score += config.getIncubatorWeight();
                tags.append(" | 인큐베이터OK");
            }

            if (isAvailable(realtime.getPrematureVentilatorAvailable())) {
                score += config.getPrematureVentilatorWeight();
                tags.append(" | 조산아호흡기OK");
            }
        }

        // 6. 응급실 혼잡도 : 병상 비율 기반으로 응급실 상태 점수 반영 / 전체 영향력은 40%로 제한
        double urgencyScore = calculateUrgencyScore(detail, config);
        score += urgencyScore * EMERGENCY_ROOM_WEIGHT_RATIO;

        // 7. NICU 스케일 :  NICU 개수에 따라 점수 증가
        if (detail.getHpnicuCount() != null
                && detail.getHpnicuCount() > 0) {

            score += Math.min(
                    detail.getHpnicuCount() * config.getNicuScaleWeight(),
                    config.getMaxNicuScaleScore()
            );
        }

        scoreEntity.updatePregnantScore(score, tags.toString());
    }

    /**
     * 응급실 혼잡도 점수 계산
     * <p>
     * - 병상 가용률 기반으로 3단계 평가
     * 1. 여유 (>50%)
     * 2. 보통 (20~50%)
     * 3. 혼잡 (<20%)
     * <p>
     * - config 값 기반으로 점수 산정
     */
    private double calculateUrgencyScore(HospitalDetail detail,
                                         WeightPregnantConfiguration config) {

        if (detail.getEmergencyBedCount() == null
                || detail.getEmergencyBedCount() <= 0) {
            return 0.0;
        }

        double ratio =
                (double) detail.getAvailableEmergencyBedCount()
                        / detail.getEmergencyBedCount();

        if (ratio > 0.5) {
            return config.getEmergencyRoomAvailableWeight();
        } else if (ratio > 0.2) { //병상이 보통이면 최대 점수의 50%만 부여
            return config.getEmergencyRoomAvailableWeight() * NORMAL_URGENCY_RATIO;
        } else if (ratio > 0) { //병상이 혼잡이면 점수 20%만 부여
            return config.getEmergencyRoomAvailableWeight() * LOW_URGENCY_RATIO;
        }

        return 0.0;
    }

    /**
     * 공통 Y/N 값 안전 체크 유틸
     *
     * @param value "Y" / "N" 문자열
     * @return Y일 때만 true 반환
     */
    private boolean isAvailable(String value) {
        return value != null && "Y".equalsIgnoreCase(value.trim());
    }

    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.PREGNANT;
    }


}