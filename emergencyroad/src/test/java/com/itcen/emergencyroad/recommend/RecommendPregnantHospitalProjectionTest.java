package com.itcen.emergencyroad.recommend;

import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.recommend.entity.HospitalScore;
import com.itcen.emergencyroad.recommend.entity.WeightPregnantConfiguration;
import com.itcen.emergencyroad.recommend.repository.WeightPregnantConfigurationRepository;
import com.itcen.emergencyroad.recommend.service.PregnantRecommendationStrategy;
import com.itcen.emergencyroad.recommend.repository.HospitalScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class RecommendPregnantHospitalProjectionTest {

    private PregnantRecommendationStrategy strategy;

    @BeforeEach
    void setUp() {
        HospitalScoreRepository repository =
                Mockito.mock(HospitalScoreRepository.class);

        WeightPregnantConfigurationRepository weightRepository =
                Mockito.mock(WeightPregnantConfigurationRepository.class);

        HospitalRepository hospitalRepository =
                Mockito.mock(HospitalRepository.class);

        strategy = new PregnantRecommendationStrategy(
                repository,
                weightRepository,
                hospitalRepository
        );
    }

    @Test
    void 분만가능하고_NICU있으면_고득점이어야한다() {

        Pregnant pregnant = Pregnant.builder()
                .deliveryAvailable("Y")
                .obstetricSurgeryAvailable("Y")
                .nicuAvailable("Y")
                .build();

        PregnantRealtime realtime = PregnantRealtime.builder()
                .isDeliveryRoomAvailable("Y")
                .incubatorAvailable("Y")
                .prematureVentilatorAvailable("Y")
                .build();

        HospitalDetail detail = HospitalDetail.builder()
                .operatingRoomCount(5)
                .emergencyBedCount(10)
                .availableEmergencyBedCount(8)
                .hpnicuCount(4)
                .build();

        WeightPregnantConfiguration config = createConfig();

        Hospital hospital = Hospital.builder()
                .hpid("A001")
                .hospitalName("테스트병원")
                .build();

        HospitalScore score = HospitalScore.builder()
                .hospital(hospital)
                .build();

        strategy.calculatePregnantScore(
                pregnant,
                realtime,
                null,
                detail,
                score,
                config
        );

        System.out.println("점수 = " + score.getPregnantScore());
        System.out.println("태그 = " + score.getPregnantTags());

        assertThat(score.getPregnantScore()).isGreaterThan(80);

        assertThat(score.getPregnantTags())
                .contains("분만가능")
                .contains("NICU보유")
                .contains("분만실여유")
                .contains("산과수술");
    }

    @Test
    void 응급실이_만석이면_추천에서_제외되어야한다() {

        HospitalDetail detail = HospitalDetail.builder()
                .operatingRoomCount(5)
                .emergencyBedCount(10)
                .availableEmergencyBedCount(0)
                .hpnicuCount(4)
                .build();

        WeightPregnantConfiguration config = createConfig();

        Hospital hospital = Hospital.builder()
                .hpid("A001")
                .hospitalName("테스트병원")
                .build();

        HospitalScore score = HospitalScore.builder()
                .hospital(hospital)
                .build();

        strategy.calculatePregnantScore(
                null,
                null,
                null,
                detail,
                score,
                config
        );

        assertThat(score.getPregnantScore()).isEqualTo(0.0);

        assertThat(score.getPregnantTags())
                .contains("응급실 만석 주의");
    }

    @Test
    void 분만이_불가능하면_추천에서_제외되어야한다() {

        Pregnant pregnant = Pregnant.builder()
                .deliveryAvailable("N")
                .build();

        HospitalDetail detail = HospitalDetail.builder()
                .emergencyBedCount(10)
                .availableEmergencyBedCount(5)
                .build();

        WeightPregnantConfiguration config = createConfig();

        Hospital hospital = Hospital.builder()
                .hpid("A001")
                .hospitalName("테스트병원")
                .build();

        HospitalScore score = HospitalScore.builder()
                .hospital(hospital)
                .build();

        strategy.calculatePregnantScore(
                pregnant,
                null,
                null,
                detail,
                score,
                config
        );

        assertThat(score.getPregnantScore()).isEqualTo(0.0);

        assertThat(score.getPregnantTags())
                .contains("분만 불가");
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