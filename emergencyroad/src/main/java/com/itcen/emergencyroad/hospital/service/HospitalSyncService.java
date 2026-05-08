package com.itcen.emergencyroad.hospital.service;

import com.itcen.emergencyroad.external.dto.EgytBassDto;
import com.itcen.emergencyroad.external.dto.EmrDto;
import com.itcen.emergencyroad.external.mapper.EgytBassMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.entity.HospitalDetail;
import com.itcen.emergencyroad.hospital.repository.HospitalDetailRepository;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//공공데이터 병원테이블에 적재 기능 수행 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalSyncService {

    private final HospitalRepository hospitalRepository;
    private final HospitalDetailRepository hospitalDetailRepository;
    private final EgytBassMapper egytBassMapper; // 매퍼 주입

    @Transactional
    public void saveOrUpdate(List<EmrDto> list) {
        for (EmrDto dto : list) {
            // 기존 데이터 조회
            Hospital existing = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (existing != null) {
                // 존재하면 업데이트
                existing.updateMaster(dto);
                log.info("기존 병원 정보 업데이트: {}", existing.getHospitalName());
            } else {
                // 존재하지 않으면 새로 생성
                Hospital newHospital = Hospital.builder()
                        .hpid(dto.getHpid())
                        .oldHospitalCode(dto.getPhpid())
                        .hospitalName(dto.getDutyName())
                        .phone(dto.getDutyTel3())
                        .build();
                hospitalRepository.save(newHospital);
                log.info("신규 병원 정보 등록: {}", newHospital.getHospitalName());
            }
        }
    }

    //5번 API -> Hospital, HospitalDetail 둘 다에게 저장
    @Transactional
    public void saveEgytBassOrUpdate(List<EgytBassDto> list) {
        if (list == null || list.isEmpty()) return;

        // 응급실 운영 여부가 true인 데이터만 뽑아서 새로운 리스트에 담기
        List<EgytBassDto> emergencyOnlyList = new ArrayList<>();
        for (EgytBassDto dto : list) {
            if (dto.getDutyEryn() != null && dto.getDutyEryn()) {
                emergencyOnlyList.add(dto);
            }
        }

        if (emergencyOnlyList.isEmpty()) {
            log.info("저장할 응급실 데이터가 없습니다.");
            return;
        }

        // 필터링된 hpid들을 리스트로 모으기
        List<String> hpids = new ArrayList<>();
        for (EgytBassDto dto : emergencyOnlyList) {
            hpids.add(dto.getHpid());
        }

        // 기존 마스터/상세 정보 한 번에 조회해서 Map에 넣기
        List<Hospital> hospitalList = hospitalRepository.findAllByHpidIn(hpids);
        Map<String, Hospital> existingHospitalMap = new java.util.HashMap<>();
        for (Hospital h : hospitalList) {
            existingHospitalMap.put(h.getHpid(), h);
        }

        List<HospitalDetail> detailList = hospitalDetailRepository.findAllByHospitalIn(hospitalList);
        Map<String, HospitalDetail> existingDetailMap = new java.util.HashMap<>();
        for (HospitalDetail d : detailList) {
            existingDetailMap.put(d.getHospital().getHpid(), d);
        }

        List<Hospital> hospitalsToSave = new ArrayList<>();
        List<HospitalDetail> detailsToSave = new ArrayList<>();


        for (EgytBassDto dto : emergencyOnlyList) {
            //Hospital
            Hospital hospital = existingHospitalMap.get(dto.getHpid());
            if (hospital == null) {
                hospital = egytBassMapper.toHospitalEntity(dto);
            } else {
                hospital.updateMasterInfo(dto);
            }
            hospitalsToSave.add(hospital);

            //Detail
            HospitalDetail detail = existingDetailMap.get(dto.getHpid());
            if (detail == null) {
                detail = egytBassMapper.toDetailEntity(dto, hospital);
            } else {
                detail.updateDetail(dto);
            }
            detailsToSave.add(detail);
        }

        // 한 번에 저장
        hospitalRepository.saveAll(hospitalsToSave);
        hospitalDetailRepository.saveAll(detailsToSave);

        log.info("응급실 데이터만 동기화 완료: {}건", emergencyOnlyList.size());
    }
}