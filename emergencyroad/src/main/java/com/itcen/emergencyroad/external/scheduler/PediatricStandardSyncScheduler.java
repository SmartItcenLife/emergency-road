package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.pediatric.service.PediatricStandardSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "scheduler.api.enabled",
        havingValue = "true"
)
public class PediatricStandardSyncScheduler {
    private final PediatricStandardSyncService pediatricStandardSyncService;
    //기준
    //@Scheduled(cron = "0 10 3 * * *") 새벽 3시 10분쯤 하루 1번
    public void sync() {
        log.info("실시간 소아 / 유아 병상 기준 데이터 가져오기 시작");

        for(String sido : RegionCode.MAP.keySet()){

            try{
                log.info("수집 대상 도시 : " + sido);
                pediatricStandardSyncService.syncBysidoForPediatricStandard(sido);
            } catch (Exception e){
                log.error("{} 지역 데이터를 불러오는데 실패했습니다.",sido,e);
            }
        }
    }
}
