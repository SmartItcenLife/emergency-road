package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.recommend.dto.projection.GeneralHospitalProjection;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightGeneralConfiguration;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightGeneralConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeneralRecommendationStrategy implements RecommendationStrategy {
    private final HospitalScoreRepository hospitalScoreRepository;
    private final HospitalRepository hospitalRepository;
    private final WeightGeneralConfigurationRepository weightRepository;

    // 혼잡도 보정 계수
    private static final double NORMAL_CONGESTION_RATIO = 0.5;
    private static final double LOW_CONGESTION_RATIO = 0.2;

    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.GENERAL;
    }

    @Override
    public void calculateScores() {
        System.out.println("===== 일반 응급 점수 계산 시작 =====");

        // 1. 일반 응급 가중치 조회
        WeightGeneralConfiguration config =
                weightRepository
                        .findTopByCategoryOrderByCreatedAtDesc(
                                HospitalCategory.GENERAL
                        )
                        .orElseThrow(() ->
                                new RuntimeException("일반 응급 가중치 설정이 없습니다.")
                        );

        // 2. 일반 응급 병원 데이터 조회
        List<GeneralHospitalProjection> results =
                hospitalRepository.findAllGeneralHospitalData();

        System.out.println("조회 병원 수 = " + results.size());

        for (GeneralHospitalProjection row : results) {

            Hospital hospital = row.getHospital();
            HospitalDetail detail = row.getDetail();
            GeneralSrsIll general = row.getGeneral();
            GeneralRealTimeAndStandard generalRealTimeAndStandard = row.getGeneralRealTimeAndStandard();

            System.out.println("=================================");
            System.out.println("병원명 = " + hospital.getHospitalName());
            System.out.println("HPID = " + hospital.getHpid());

            System.out.println("detail = " + detail);
            System.out.println("general = " + general);
            System.out.println("realtime = " + generalRealTimeAndStandard);

            if (generalRealTimeAndStandard != null) {
                System.out.println("emergencyAvailableBeds(가용 응급실 수) = " + generalRealTimeAndStandard.getEmergencyAvailableBeds());
                System.out.println("emergencyTotalBeds(전체 응급실 수) = " + generalRealTimeAndStandard.getEmergencyTotalBeds());
            }

            // 기존 점수 조회
            HospitalScore scoreEntity =
                    hospitalScoreRepository.findByHospital_Hpid(hospital.getHpid())
                            .orElse(null);

            // 없으면 생성
            if (scoreEntity == null) {
                System.out.println("HospitalScore 신규 생성");
                scoreEntity = HospitalScore.builder()
                        .hospital(hospital)
                        .build();
            }
            // 점수 계산
            calculateGeneralScore(
                    generalRealTimeAndStandard,
                    general,
                    detail,
                    scoreEntity,
                    config
            );
            System.out.println("최종 generalScore = "
                    + scoreEntity.getGeneralScore());

            System.out.println("최종 tags = "
                    + scoreEntity.getGeneralTags());

            hospitalScoreRepository.save(scoreEntity);

            System.out.println("DB 저장 완료");

        }
        System.out.println("===== 일반 응급 점수 계산 종료 =====");
    }

    //점수 계산
    public void calculateGeneralScore(
            GeneralRealTimeAndStandard realtime,
            GeneralSrsIll severe,
            HospitalDetail detail,
            HospitalScore scoreEntity,
            WeightGeneralConfiguration config
    ) {
        System.out.println("----- 점수 계산 시작 -----");
        double score = 0.0;
        StringBuilder tags = new StringBuilder();
        System.out.println("realtime = " + realtime);
        // 1. FILTER
        if (realtime == null
                || realtime.getEmergencyAvailableBeds() == null
                || realtime.getEmergencyAvailableBeds() <= 0) {
            System.out.println("FILTER 걸림 - 응급실 만석");
            scoreEntity.updateGeneralScore(0.0, "응급실 만석");
            return;
        }

        // 2. 응급실 기본 점수
        score += config.getEmergencyRoomWeight();
        tags.append("응급실가능");

        // 3. 중증질환 대응
        int severeCount = 0;

        if (severe != null) {
            // 뇌출혈
            if (isAvailable(severe.getMKioskTy1())) {
                severeCount++;
                tags.append(" | 뇌출혈 대응 가능");
            }

            // 뇌경색
            if (isAvailable(severe.getMKioskTy2())) {
                severeCount++;
                tags.append(" | 뇌경색 대응 가능");
            }

            // 심근경색
            if (isAvailable(severe.getMKioskTy3())) {
                severeCount++;
                tags.append(" | 심근경색 대응 가능");
            }

            // 중증화상
            if (isAvailable(severe.getMKioskTy5())) {
                severeCount++;
                tags.append(" | 화상 대응 가능");
            }

            // 응급투석
            if (isAvailable(severe.getMKioskTy23())) {
                severeCount++;
                tags.append(" | 응급투석 가능");
            }
        }
        System.out.println("중증질환 개수 = " + severeCount);
        score += severeCount * config.getSevereDiseaseWeight();

        // 4. ICU
        int icuCount = 0;

        if (realtime.getIcuAvailableBeds() != null
                && realtime.getIcuAvailableBeds() > 0) icuCount++;

        if (realtime.getNeuroIcuAvailableBeds() != null
                && realtime.getNeuroIcuAvailableBeds() > 0) icuCount++;

        if (realtime.getChestIcuAvailableBeds() != null
                && realtime.getChestIcuAvailableBeds() > 0) icuCount++;

        score += icuCount * config.getIcuWeight();
        System.out.println("ICU 개수 = " + icuCount);

        // 5. 장비
        // CT
        if (isAvailable(realtime.getCtAvailable())) {
            score += config.getEquipmentWeight();
            tags.append(" | CT");
        }

        // MRI
        if (isAvailable(realtime.getMriAvailable())) {
            score += config.getEquipmentWeight();
            tags.append(" | MRI");
        }
        // ECMO
        if (isAvailable(realtime.getEcmoAvailable())) {
            score += config.getEcmoBonus();
            tags.append(" | ECMO");
        }
        // CRRT
        if (isAvailable(realtime.getCrrtAvailable())) {
            score += config.getCrrtBonus();
            tags.append(" | CRRT");
        }
        // 혈관조영
        if (isAvailable(realtime.getAngioAvailable())) {
            score += config.getAngioBonus();
            tags.append(" | 혈관조영");
        }

        // 6. 응급실 혼잡도
        double congestionScore = calculateCongestionScore(realtime, config);
        score += congestionScore;

        System.out.println("최종 계산 점수 = " + score);
        System.out.println("최종 태그 = " + tags);

        scoreEntity.updateGeneralScore(score, tags.toString());
        System.out.println("혼잡도 점수 = " + congestionScore);
    }

    //emergencyAvailableBeds = 실시간 가용 병상
    //emergencyTotalBeds = 응급실 기준 병상 수 => 가용 병상 / 전체 응급 병상
    //혼잡도 계산
    private double calculateCongestionScore(
            GeneralRealTimeAndStandard realtime,
            WeightGeneralConfiguration config
    ) {

        if (realtime.getEmergencyTotalBeds() == null
                || realtime.getEmergencyTotalBeds() <= 0) {
            return 0.0;
        }

        double ratio =
                (double) realtime.getEmergencyAvailableBeds()
                        / realtime.getEmergencyTotalBeds();

        if (ratio > 0.5) {
            return config.getCongestionWeight();

        } else if (ratio > 0.2) {
            return config.getCongestionWeight()
                    * NORMAL_CONGESTION_RATIO;

        } else if (ratio > 0) {
            return config.getCongestionWeight()
                    * LOW_CONGESTION_RATIO;
        }
        return 0.0;
    }

    private boolean isAvailable(String value) {
        return value != null
                && "Y".equalsIgnoreCase(value.trim());
    }
}
