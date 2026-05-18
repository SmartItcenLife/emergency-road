package com.itcen.emergencyroad.findpath.controller;

import com.itcen.emergencyroad.findpath.dto.LocationRequestDto;
import com.itcen.emergencyroad.findpath.dto.PathResponseDto;
import com.itcen.emergencyroad.findpath.service.TmapService;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 데이터(JSON)만 반환하는 컨트롤러
@RequestMapping("/api/tmap/path") // 티맵 전용 URL 주소 설정
@RequiredArgsConstructor
public class TmapController {

    private final TmapService tmapService;
    private final HospitalRepository hospitalRepository; // 병원 데이터를 가져오기 위한 리포지토리 부착

    @PostMapping("/hospitals")
    public ResponseEntity<List<PathResponseDto>> findHospitals(@RequestBody LocationRequestDto requestDto) {

        // 1. DB에 저장되어 있는 전체 병원 리스트를 가져옵니다.
        List<Hospital> hospitals = hospitalRepository.findAll();

        // 2. TmapService에게 사용자 위치와 병원 리스트를 전달하여 거리/시간을 계산합니다.
        List<PathResponseDto> resultList = tmapService.findHospitalsWithDistanceTmap(requestDto, hospitals);

        // 3. 계산된 결과 리스트를 반환합니다.
        return ResponseEntity.ok(resultList);
    }
}