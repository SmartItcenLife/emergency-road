package com.itcen.emergencyroad.recommend;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalDetailRepository;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import com.itcen.emergencyroad.pregnant.repository.PregnantRealtimeRepository;
import com.itcen.emergencyroad.pregnant.repository.PregnantRepository;
import com.itcen.emergencyroad.pregnant.repository.PregnantStandardRepository;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import com.itcen.emergencyroad.recommend.service.PregnantRecommendationStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PregnantHospitalProjectionRecommendationIntegrationTest {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalDetailRepository hospitalDetailRepository;

    @Autowired
    private PregnantRepository pregnantRepository;

    @Autowired
    private PregnantRealtimeRepository realtimeRepository;

    @Autowired
    private PregnantStandardRepository standardRepository;

    @Autowired
    private PregnantRecommendationStrategy strategy;

    @Test
    void 실제_병원_데이터로_점수_계산() {

        // given
        Hospital hospital = hospitalRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        HospitalDetail detail =
                hospitalDetailRepository.findByHospital(hospital)
                        .orElse(null);

        Pregnant pregnant =
                pregnantRepository.findPregnantByHospital(hospital)
                        .orElse(null);

        PregnantRealtime realtime =
                realtimeRepository.findPregnantRealtimeByHospital(hospital)
                        .orElse(null);

        PregnantStandard standard =
                standardRepository.findPregnantStandardByHospital(hospital)
                        .orElse(null);

        HospitalScore score = HospitalScore.builder()
                .hospital(hospital)
                .build();

        // when
        strategy.calculatePregnantScore(
                pregnant,
                realtime,
                standard,
                detail,
                score,
                createConfig()
        );

        // then
        System.out.println("========== 실제 데이터 테스트 ==========");
        System.out.println("병원명 = " + hospital.getHospitalName());
        System.out.println("HPID = " + hospital.getHpid());

        if (detail != null) {
            System.out.println("응급실 병상 수 = " + detail.getEmergencyBedCount());
            System.out.println("사용 가능 병상 수 = " + detail.getAvailableEmergencyBedCount());
        }

        if (pregnant != null) {
            System.out.println("분만 가능 여부 = " + pregnant.getDeliveryAvailable());
            System.out.println("NICU 여부 = " + pregnant.getNicuAvailable());
        }

        System.out.println("최종 점수 = " + score.getPregnantScore());
        System.out.println("태그 = " + score.getPregnantTags());

        assertThat(score.getPregnantScore())
                .isGreaterThanOrEqualTo(0.0);

        assertThat(score.getPregnantTags())
                .isNotNull();
    }

    private WeightPregnantConfiguration createConfig() {
        return WeightPregnantConfiguration.builder()
                .deliveryAvailableWeight(40.0)
                .obstetricSurgeryWeight(20.0)
                .nicuAvailableWeight(10.0)
                .deliveryRoomAvailableWeight(30.0)
                .emergencyRoomAvailableWeight(30.0)

                .incubatorWeight(10.0)
                .prematureVentilatorWeight(5.0)
                .operatingRoomBonusWeight(5.0)
                .nicuScaleWeight(2.0)
                .maxNicuScaleScore(10.0)
                .operatingRoomThreshold(3)
                .build();
    }
}