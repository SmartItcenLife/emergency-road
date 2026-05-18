package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.service.EmrSyncService;
import com.itcen.emergencyroad.general.service.GeneralService;
import com.itcen.emergencyroad.pregnant.service.PregnantRealtimeSyncService;
import com.itcen.emergencyroad.hospital.service.HospitalSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//응급실 실시간 가용병상정보 조회 오퍼레이션 API 스케쥴링
// 우선 시(ex. 서울특별시) 기준으로 데이터 끌어오기
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "scheduler.api.enabled",
        havingValue = "true"
)
public class EmrSyncScheduler {

    private final EmrSyncService emrSyncService;
    private final HospitalSyncService hospitalSyncService;
    private final PregnantRealtimeSyncService pregnantRealtimeSyncService;
    private  final GeneralService generalService;

    // 2분 주기 + 첫 실행은 서버 켜지고 10초 대기 후 시작 (트래픽 분산)
    //@Scheduled(initialDelay = 10000, fixedDelay = 180000)  //3분
    public void sync() {

        log.info("EMR API 가져오기 시작");

        for (String sido : RegionCode.MAP.keySet()) {

            //List<String> sigunguList = RegionCode.MAP.getOrDefault(sido, List.of());

           // for (String sigungu : sigunguList) {

                try {
                    List<EmrDto> list = emrSyncService.fetchAll(sido);

                    hospitalSyncService.saveOrUpdate(list);
                    pregnantRealtimeSyncService.saveOrUpdate(list);
                    generalService.saveOrUpdate(list);


                    log.info("{} emrSyncService 동기화 완료 size={}", sido, list.size());

                    log.info("{} generalService 동기화 완료 size={}", sido, list.size());

                    if (!list.isEmpty()) {
                        log.info("sample hpId={}", list.get(0).getHpid());
                    }

                } catch (Exception e) {
                    log.error("{} 동기화 실패", sido, e);
                }
        }
    }
}