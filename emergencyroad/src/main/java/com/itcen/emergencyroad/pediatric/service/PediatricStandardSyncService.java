package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeMkiosktyDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricStandardDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricStandard;
import com.itcen.emergencyroad.pediatric.repository.PediatricStandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricStandardSyncService {
    private final PediatricRealtimeStatusApiClient pediatricRealtimeStatusApiClient;
    private final PediatricStandardRepository pediatricStandardRepository;
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

        if (responseDto.getResponse().getBody().getItems() == null) {
            System.out.println("조회 결과가 없습니다. (Empty Response)");
            return;
        }

        List<PediatricStandardDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        if (items == null || items.isEmpty()) {
            System.out.println("처리할 아이템이 없습니다.");
            return;
        }

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
