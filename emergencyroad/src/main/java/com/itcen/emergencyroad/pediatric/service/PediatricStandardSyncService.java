package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricStandardDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.pediatric.repository.PediatricStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricStandardSyncService {
    private final PediatricRealtimeStatusApiClient pediatricRealtimeStatusApiClient;
    private final PediatricStandardRepository pediatricStandardRepository;
    private final PediatricApiResponseParser pediatricApiResponseParser;
    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;
    private final EmrMapper emrMapper;

    @Transactional
    public void syncBysidoForPediatricStandard(String sido, int page, int rows){
        String json = pediatricRealtimeStatusApiClient.getPediatricRealtimeRawBySido(sido, page, rows);


        PediatricRealtimeApiResponseDto<PediatricStandardDto> responseDto =
                objectMapper.readValue(
                        json,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        PediatricRealtimeApiResponseDto.class,
                                        PediatricStandardDto.class
                                )
                );

        List<PediatricStandardDto> items =
                pediatricApiResponseParser.extractItemsOrEmpty(responseDto,"소아 병상 기준정보" );

        for ( PediatricStandardDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (hospital == null) {
                System.out.println("병원 없음, Skip : " + dto.getHpid());
                continue;
            }

            PediatricStandard entity = pediatricStandardRepository.findByHospital(hospital)
                    .orElseGet(()-> emrMapper.toPediatricStandardEntity(dto,hospital));

            if (entity.getId() != null){
                emrMapper.updatePediatricStandardEntity(entity,dto);
            }
            pediatricStandardRepository.save(entity);
        }
    }
}
