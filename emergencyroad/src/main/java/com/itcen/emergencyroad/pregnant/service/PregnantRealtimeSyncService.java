package com.itcen.emergencyroad.pregnant.service;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.PregnantRealtime;
import com.itcen.emergencyroad.pregnant.entity.PregnantStandard;
import com.itcen.emergencyroad.pregnant.repository.PregnantRealtimeRepository;
import com.itcen.emergencyroad.pregnant.repository.PregnantRepository;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pregnant.repository.PregnantStandardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

//공공데이터 임산부 테이블 적재 기능 수행 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class PregnantRealtimeSyncService {

    private final PregnantRepository pregnantRepository;
    private final PregnantRealtimeRepository pregnantRealtimeRepository;
    private final PregnantStandardRepository pregnantStandardRepository;
    private final HospitalRepository hospitalRepository;
    private final EmrMapper emrMapper;

    @Transactional
    public void saveOrUpdate(List<EmrDto> list) {

        for (EmrDto dto : list) {
            log.info("dto hpid = {}", dto.getHpid());

            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid())
                    .orElse(null);
            log.info("hospital exists = {}", hospital != null);
            if (hospital == null) continue;

            //임산부 실시간 가용 병상 테이블 적재
            PregnantRealtime realtimeEntity = pregnantRealtimeRepository.findPregnantRealtimeByHospital(hospital)
                    .orElse(null);

            if (realtimeEntity == null) {
                PregnantRealtime newEntity = emrMapper.toPregnantRealTimeStatusEntity(dto, hospital);
                pregnantRealtimeRepository.save(newEntity);

            } else {
                realtimeEntity.update(dto);
            }

            //기준 테이블 적재
            PregnantStandard standardEntity = pregnantStandardRepository.findPregnantStandardByHospital(hospital)
                    .orElse(null);
            if (standardEntity == null) {

                PregnantStandard newEntity = emrMapper.toPregnantStandardStatusEntity(dto, hospital);
                pregnantStandardRepository.save(newEntity);

            } else {
                standardEntity.update(dto);
            }
        }
    }
}
