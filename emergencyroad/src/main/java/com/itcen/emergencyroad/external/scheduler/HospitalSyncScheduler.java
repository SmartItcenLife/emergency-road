package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.external.api.MessageApiClient;
import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.service.MessageSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HospitalSyncScheduler {

    private final MessageSyncService messageSyncService;

    @Scheduled(fixedDelay = 3000000)
    public void sync() {

        log.info("EMR API 가져오기 시작");

        for (String sido : RegionCode.MAP.keySet()) {

            //List<String> sigunguList = RegionCode.MAP.getOrDefault(sido, List.of());

            // for (String sigungu : sigunguList) {

            try {
              messageSyncService.syncBySido(sido,1, 100);

            } catch (Exception e) {
                log.error("{} 동기화 실패", sido, e);
            }
        }
    }

}
