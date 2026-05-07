package com.itcen.emergencyroad.pregnant.service;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import com.itcen.emergencyroad.pregnant.repository.PregnantRepository;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

//공공데이터 임산부 테이블 적재 기능 수행 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class PregnantSyncService {

    private final PregnantRepository pregnantRepository;
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

            Pregnant entity = pregnantRepository.findByHospital(hospital)
                    .orElse(null);

            if (entity == null) {

                Pregnant newEntity = emrMapper.toEntity(dto, hospital);
                pregnantRepository.save(newEntity);

            } else {

                emrMapper.updateEntity(entity, dto);
            }
        }
    }
}
