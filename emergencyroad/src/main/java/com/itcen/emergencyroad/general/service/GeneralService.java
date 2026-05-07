package com.itcen.emergencyroad.general.service;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.general.entity.General;
import com.itcen.emergencyroad.general.entity.Srsill;
import com.itcen.emergencyroad.general.repository.GeneralRepository;
import com.itcen.emergencyroad.general.repository.SrsIllRepository;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pregnant.entity.Pregnant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralService {
    private final GeneralRepository generalRepository;
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

            General entity = generalRepository.findByHospital(hospital)
                    .orElse(null);

            if (entity == null) {

                General newEntity = emrMapper.toGeneralEntity(dto, hospital);
                generalRepository.save(newEntity);
            } else {
                emrMapper.updateGenralEntity(entity, dto);
            }
        }
    }
}
