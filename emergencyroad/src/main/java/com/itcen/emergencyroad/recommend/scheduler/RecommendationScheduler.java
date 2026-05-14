package com.itcen.emergencyroad.recommend.scheduler;

import com.itcen.emergencyroad.recommend.service.HospitalRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationScheduler {

    private final HospitalRecommendationService recommendationService;


    @Scheduled(cron = "0 37 12 * * *")
    public void updateBaseHospitalScores() {

        recommendationService.calculateAllHospitalScores();

        log.info("병원 추천 점수(위치 제외) 전체 업데이트 완료");
    }
}
