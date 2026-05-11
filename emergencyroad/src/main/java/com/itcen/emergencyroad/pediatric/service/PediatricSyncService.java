package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricSyncService {
    private final PediatricRealtimeStatusApiClient pediatricRealtimeStatusApiClient;
    private final PediatricRealtimeRepository pediatricRealtimeRepository;
    private final PediatricApiResponseParser pediatricApiResponseParser;
    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;
    private final EmrMapper emrMapper;

    @Transactional
    public void syncBySidoForPediatric(String sido, int page, int rows) throws Exception {
        String json = pediatricRealtimeStatusApiClient.getPediatricRealtimeRawBySido(sido, page, rows);

        PediatricRealtimeApiResponseDto<PediatricRealtimeDto> responseDto =
                objectMapper.readValue(
                        json,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        PediatricRealtimeApiResponseDto.class,
                                        PediatricRealtimeDto.class
                                )
                );

        List<PediatricRealtimeDto> items =
                pediatricApiResponseParser.extractItemsOrEmpty(responseDto, "소아 실시간 병상");


        for (PediatricRealtimeDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (hospital == null) {
                System.out.println("병원 없음, skip : " + dto.getHpid());
                continue;
            }

            PediatricRealtime entity = pediatricRealtimeRepository.findByHospital(hospital)
                    .orElseGet(() -> emrMapper.toPediatricEntity(dto, hospital));

            if (entity.getId() != null) {
                emrMapper.updatePediatricEntity(entity, dto);
            }

            pediatricRealtimeRepository.save(entity);
        }
    }
}
