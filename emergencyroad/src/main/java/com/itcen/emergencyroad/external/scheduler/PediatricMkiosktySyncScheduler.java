package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.pediatric.service.PediatricMkiosktySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PediatricMkiosktySyncScheduler {

    private final PediatricMkiosktySyncService pediatricMkiosktySyncService;

    @Scheduled(fixedDelay = 3000000)
    public void sync() {
        log.info("소아 대상 : 중증질환 수용여부 데이터 조회시작");
        for (Map.Entry<String, List<String>> entry : RegionCode.MAP.entrySet()) {
            String stage1 = entry.getKey();
            List<String> stage2List = entry.getValue();

            for (String stage2 : stage2List) {
                try {
                    log.info("수집 대상 지역 : {} {}", stage1, stage2);
                    pediatricMkiosktySyncService.syncByAddrForPediatricMkioskty(stage1, stage2);
                    Thread.sleep(5000);
                } catch (Exception e) {
                    log.error("동기화 실패 : {} {}", stage1, stage2, e);
                    }
                }
            }
        }
}
