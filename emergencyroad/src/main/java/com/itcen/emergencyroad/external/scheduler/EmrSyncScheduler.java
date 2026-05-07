package com.itcen.emergencyroad.external.scheduler;

import com.itcen.emergencyroad.external.RegionCode;
import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.service.EmrSyncService;
import com.itcen.emergencyroad.pregnant.service.PregnantSyncService;
import com.itcen.emergencyroad.hospital.service.HospitalSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//응급실 실시간 가용병상정보 조회 오퍼레이션 API 스케쥴링
// 우선 시(ex. 서울특별시) 기준으로 데이터 끌어오기
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("prod") // 앱실행 시 자동 실행 방지.
public class EmrSyncScheduler {

    private final EmrSyncService emrSyncService;
    private final HospitalSyncService hospitalSyncService;
    private final PregnantSyncService pregnantSyncService;

    @Scheduled(fixedDelay = 3000000)
    public void sync() {

        log.info("EMR API 가져오기 시작");

        for (String sido : RegionCode.MAP.keySet()) {

            //List<String> sigunguList = RegionCode.MAP.getOrDefault(sido, List.of());

           // for (String sigungu : sigunguList) {

                try {
                    List<EmrDto> list = emrSyncService.fetchAll(sido);

                    hospitalSyncService.saveOrUpdate(list);

                    pregnantSyncService.saveOrUpdate(list);
                    log.info("{} 동기화 완료 size={}", sido, list.size());

                    if (!list.isEmpty()) {
                        log.info("sample hpId={}", list.get(0).getHpid());
                    }

                } catch (Exception e) {
                    log.error("{} 동기화 실패", sido, e);
                }
        }
    }
}