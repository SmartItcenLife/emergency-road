package com.itcen.emergencyroad.general.service;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.general.dto.SrsillDTO;
import com.itcen.emergencyroad.general.entity.Srsill;
import com.itcen.emergencyroad.general.repository.SrsIllRepository;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SrsIllService {
    private final SrsIllRepository srsIllRepository;
    private final HospitalRepository hospitalRepository;
    private final EmrMapper emrMapper;

    @Transactional
    public void saveOrUpdate(List<EmrDto> list) {

        for (EmrDto dto : list) {
            log.info("dto hpid = {}", dto.getHpid());

            // 1. 병원 존재 여부 확인
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid())
                    .orElse(null);

            if (hospital == null) {
                log.warn("Hospital not found for hpid: {}", dto.getHpid());
                continue;
            }

            // 2. 기존 중증 질환 정보 조회
            Srsill entity = srsIllRepository.findByHospital(hospital)
                    .orElse(null);

            if (entity == null) {
                // 3-A. 신규 저장이면 매퍼를 통해 엔티티 생성 후 저장
                Srsill newEntity = emrMapper.toSrsillEntity(dto, hospital);
                srsIllRepository.save(newEntity);
            } else {
                // 3-B. 기존 데이터가 있으면 매퍼를 통해 필드 업데이트 (더티 체킹 발동)
                emrMapper.updateSrsillData(entity, dto);
            }
        }
    }

}
