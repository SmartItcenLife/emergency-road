package com.itcen.emergencyroad.recommend.service;

import com.itcen.emergencyroad.general.entity.GeneralRealTimeAndStandard;
import com.itcen.emergencyroad.general.entity.GeneralSrsIll;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.recommend.dto.projection.PediatricHospitalProjection;
import com.itcen.emergencyroad.recommend.entity.HospitalCategory;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightGeneralConfiguration;
import com.itcen.emergencyroad.recommend.entity.WeightPediatricConfiguration;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import com.itcen.emergencyroad.recommend.repository.WeightPediatricConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PediatricRecommendationStrategy implements RecommendationStrategy {
    private final HospitalScoreRepository hospitalScoreRepository;
    private final HospitalRepository hospitalRepository;
    private final WeightPediatricConfigurationRepository weightRepository;

    // 특수질환 기본 가산 개수 기준
    private static final int MIN_AVAILABLE_COUNT = 0;


    @Override
    public HospitalCategory getCategory() {
        return HospitalCategory.PEDIATRIC;
    }

    @Override
    public void calculateScores() {
        System.out.println("=====  응급 점수 계산 시작 =====");

        // 1. 일반 응급 가중치 조회
        WeightPediatricConfiguration config =
                weightRepository
                        .findTopByCategoryOrderByCreatedAtDesc(
                                HospitalCategory.PEDIATRIC
                        )
                        .orElseThrow(() ->
                                new RuntimeException("소아 응급 가중치 설정이 없습니다.")
                        );

        // 2. 일반 응급 병원 데이터 조회
        List<PediatricHospitalProjection> results =
                hospitalRepository.findAllHospitalPediatricData();

        System.out.println("조회 병원 수 = " + results.size());

        for (PediatricHospitalProjection row : results) {

            Hospital hospital = row.getHospital();
            HospitalDetail detail = row.getDetail();
            PediatricMkioskty pediatricMkioskty = row.getPediatricMkioskty();
            PediatricRealtime pediatricRealtime = row.getPediatricRealtime();
            PediatricStandard pediatricStandard = row.getPediatricStandard();


            System.out.println("=================================");
            System.out.println("병원명 = " + hospital.getHospitalName());
            System.out.println("HPID = " + hospital.getHpid());

            System.out.println("detail = " + detail);
            System.out.println("pediatricMkioskty = " + pediatricMkioskty);
            System.out.println("pediatricRealtime = " + pediatricRealtime);
            System.out.println("pediatricStandard = " + pediatricStandard);

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
            calculatePediatricScore(
                    pediatricRealtime,
                    pediatricStandard,
                    pediatricMkioskty,
                    detail,
                    scoreEntity,
                    config
            );
            System.out.println("최종 pediatricScore = "
                    + scoreEntity.getPediatricScore());

            System.out.println("최종 pediatricTags = "
                    + scoreEntity.getPediatricTags());

            hospitalScoreRepository.save(scoreEntity);

            System.out.println("DB 저장 완료");
        }
        System.out.println("===== 소아 응급 점수 계산 종료 =====");
    }

    //점수 계산
    // 소아 점수 계산
    public void calculatePediatricScore(
            PediatricRealtime realtime,
            PediatricStandard standard,
            PediatricMkioskty mkiosk,
            HospitalDetail detail,
            HospitalScore scoreEntity,
            WeightPediatricConfiguration config
    ) {

        double score = 0.0;
        StringBuilder tags = new StringBuilder();

        System.out.println("----- 소아 점수 계산 시작 -----");

        // 1. FILTER
        if (realtime == null) {

            scoreEntity.updatePediatricScore(
                    0.0,
                    "소아 실시간 정보 없음"
            );

            return;
        }

        // 2. 기본 소아 응급 가능
        if (realtime.getPediatricBedCount() != null
                && realtime.getPediatricBedCount() > MIN_AVAILABLE_COUNT) {

            score += config.getPediatricEmergencyWeight();
            tags.append("소아병상");
        }

        // 3. 소아 ICU
        if (realtime.getPediatricIcuCount() != null
                && realtime.getPediatricIcuCount() > MIN_AVAILABLE_COUNT) {

            score += config.getPediatricIcuWeight();
            tags.append(" | 소아중환자실");
        }

        // 4. 응급전용 소아 ICU
        if (realtime.getPediatricEmergencyIcuCount() != null
                && realtime.getPediatricEmergencyIcuCount() > MIN_AVAILABLE_COUNT) {

            score += config.getPediatricEmergencyIcuWeight();
            tags.append(" | 응급소아ICU");
        }

        // 5. 인큐베이터
        if (isAvailable(realtime.getIncubatorResourceAvailable())) {

            score += config.getIncubatorWeight();
            tags.append(" | 인큐베이터");
        }

        // 6. 조산아 인공호흡기
        if (isAvailable(realtime.getPreemieVentiAvailable())) {

            score += config.getPreemieVentiWeight();
            tags.append(" | 조산아호흡기");
        }

        // 7. 소아 인공호흡기
        if (isAvailable(realtime.getPediatricVentiAvailable())) {

            score += config.getPediatricVentiWeight();
            tags.append(" | 소아호흡기");
        }

        // 8. 음압격리
        if (realtime.getPediatricNegativeIsolationCount() != null
                && realtime.getPediatricNegativeIsolationCount() > MIN_AVAILABLE_COUNT) {

            score += config.getNegativeIsolationWeight();
            tags.append(" | 음압격리");
        }

        // 9. 일반격리
        if (realtime.getPediatricGeneralIsolationCount() != null
                && realtime.getPediatricGeneralIsolationCount() > MIN_AVAILABLE_COUNT) {

            score += config.getIsolationWeight();
            tags.append(" | 일반격리");
        }

        // 10. 특수 소아 질환 대응
        int specialCount = 0;

        if (mkiosk != null) {

            // 장중첩
            if (isAvailable(mkiosk.getPediatricBowelObstructionAvailable())) {

                specialCount++;
                score += config.getSpecialTreatmentWeight();

                tags.append(" | 장중첩");
            }

            // 소아 위장관 내시경
            if (isAvailable(mkiosk.getPediatricEmergencyEndoscopyGastroAvailable())) {

                specialCount++;
                score += config.getSpecialTreatmentWeight();

                tags.append(" | 소아위장관내시경");
            }

            // 기관지 내시경
            if (isAvailable(mkiosk.getPediatricEmergencyEndoscopyBronchialAvailable())) {

                specialCount++;
                score += config.getSpecialTreatmentWeight();

                tags.append(" | 기관지내시경");
            }

            // 저체중 출생아
            if (isAvailable(mkiosk.getLowBirthWeightInfantAvailable())) {

                specialCount++;

                score += config.getSpecialTreatmentWeight();
                score += config.getLowBirthWeightBonus();

                tags.append(" | 저체중출생아");
            }

            // 혈관중재
            if (isAvailable(mkiosk.getPediatricVascularInterventionAvailable())) {

                specialCount++;
                score += config.getSpecialTreatmentWeight();

                tags.append(" | 소아혈관중재");
            }
        }

        System.out.println("특수질환 대응 수 = " + specialCount);

        System.out.println("최종 pediatricScore = " + score);
        System.out.println("최종 tags = " + tags);

        scoreEntity.updatePediatricScore(
                score,
                tags.toString()
        );
    }

    private boolean isAvailable(String value) {

        return value != null
                && "Y".equalsIgnoreCase(value.trim());
    }
}