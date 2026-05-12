package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.pediatric.service.PediatricSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PediatricRealtimeSyncScheduler {

    private final PediatricSyncService pediatricSyncService;

   // @Scheduled(fixedDelay = 3000000)
    public void sync() {
        log.info("실시간 소아 / 유아 데이터 가져오기 시작");

        for(String sido : RegionCode.MAP.keySet()){

            try{
                log.info("수집 대상 도시 : " + sido);
                pediatricSyncService.syncBySidoForPediatric(sido);
            } catch (Exception e){
                log.error("{} 지역 데이터를 불러오는데 실패했습니다.",sido,e);
            }
        }
    }
}
