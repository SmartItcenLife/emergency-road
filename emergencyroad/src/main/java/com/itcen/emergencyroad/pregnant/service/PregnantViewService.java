package com.itcen.emergencyroad.pregnant.service;

import com.itcen.emergencyroad.pregnant.dto.PregnantHospitalDetailDto;
import com.itcen.emergencyroad.pregnant.repository.PregnantRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PregnantViewService {
    private final PregnantRealtimeRepository pregnantRealtimeRepository;

    public PregnantHospitalDetailDto findPregnantRealtimeByHospital(String hpid) {
        return pregnantRealtimeRepository.findPregnantHospitalDetail(hpid)
                .orElseThrow(() -> new IllegalArgumentException("임산부 병원 상세 정보가 없습니다. hpid=" + hpid));
    }
}
