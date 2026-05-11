package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.pediatric.dto.PediatricHospitalListDto;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricViewService {
    private final PediatricRealtimeRepository pediatricRealtimeRepository;

    // Query 결과 조회
    public List<PediatricHospitalListDto> getPediatricHospitalList(){
        return pediatricRealtimeRepository.findPediatricHospitalList();
    }
}
