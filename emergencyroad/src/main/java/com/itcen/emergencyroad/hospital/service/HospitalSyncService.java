package com.itcen.emergencyroad.hospital.service;

import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//공공데이터 병원테이블에 적재 기능 수행 서비스
@Service
@RequiredArgsConstructor
public class HospitalSyncService {

    private final HospitalRepository hospitalRepository;

    @Transactional
    public void saveOrUpdate(List<EmrDto> list) {

        for (EmrDto dto : list) {

            Hospital existing = hospitalRepository.findByHpid(dto.getHpid())
                    .orElse(null);

            Hospital hospital = Hospital.builder()
                    .id(existing != null ? existing.getId() : null)
                    .hpid(dto.getHpid())
                    .hospitalName(dto.getDutyName())
                    .phone(dto.getDutyTel3())
                    .build();

            hospitalRepository.save(hospital);
        }
    }
}