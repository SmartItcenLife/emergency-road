package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.dto.EgytBassDto;
import com.itcen.emergencyroad.external.service.EgytBassInfoSyncService;
import com.itcen.emergencyroad.hospital.service.HospitalSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class EgytBassInfoSyncScheduler {


    private final EgytBassInfoSyncService egytBassInfoSyncService;//공공 api 호출 메서드를 담고 있는 서비스
    private final HospitalSyncService hospitalSyncService;

    //새벽 3시 하루 1회
//    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(
//            initialDelay = 6000,
//            fixedDelay = 3000000
//    )
    public void sync() {

        log.info("EgytBassInfo API 가져오기 시작");

        try {
            List<EgytBassDto> list = egytBassInfoSyncService.fetchAll();
            log.info("공공 API 조회 완료");

            //hospitalDetailSyncService.saveOrUpdate(list);
            hospitalSyncService.saveEgytBassOrUpdate(list); //여기서 Hospital, Detail에 모두 저장 가능 (최적화)
            log.info("HospitalDetail 저장 완료");

        } catch (Exception e) {
            log.error("병원 상세정보 동기화 실패", e);
        }
    }
}