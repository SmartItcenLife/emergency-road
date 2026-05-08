package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricMkiosktyApiClient;
import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeMkiosktyDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.repository.PediatricMkiosktyRepository;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricMkiosktySyncService {
    private final PediatricMkiosktyApiClient pediatricMkiosktyApiClient;
    private final PediatricMkiosktyRepository pediatricMkiosktyRepository;
    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void syncByAddrForPediatricMkioskty(String stage1, String stage2, int page, int row){
        String json = pediatricMkiosktyApiClient.getPediatricMkiosktyRawByAddr(stage1, stage2, page, row);

        PediatricRealtimeApiResponseDto<PediatricRealtimeMkiosktyDto> responseDto = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory()
                        .constructParametricType(
                                PediatricRealtimeApiResponseDto.class,
                                PediatricRealtimeMkiosktyDto.class
                        )
        );
        List<PediatricRealtimeMkiosktyDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (PediatricRealtimeMkiosktyDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (hospital == null) {
                System.out.println("병원 없음, skip : " + dto.getHpid());
                continue;
            }
            PediatricMkioskty entity = pediatricMkiosktyRepository.findByHospital(hospital)
                    .orElse(
                            PediatricMkioskty.builder()
                                    .hospital(hospital)
                                    .build()
                    );

            entity.update(
                    dto.getMkioskty10(),        // 장중첩/폐색 영유아
                    dto.getMkioskty12(),        // 응급내시경 영유아 위장관
                    dto.getMkioskty14(),        // 응급내시경 영유아 기관지
                    dto.getMkioskty15(),        // 저체중출생아
                    dto.getMkioskty27(),        // 영상의학혈관중재 영유아
                    dto.getMkioskty10Msg(),
                    dto.getMkioskty12Msg(),
                    dto.getMkioskty14Msg(),
                    dto.getMkioskty15Msg(),
                    dto.getMkioskty27Msg()
            );

            pediatricMkiosktyRepository.save(entity);
        }

    }
}
