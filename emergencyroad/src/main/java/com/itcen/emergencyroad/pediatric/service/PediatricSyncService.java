package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricStandardDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricRealtime;
import com.itcen.emergencyroad.pediatric.repository.PediatricMkiosktyRepository;
import com.itcen.emergencyroad.pediatric.repository.PediatricRealtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PediatricSyncService {
    private final PediatricRealtimeStatusApiClient pediatricRealtimeStatusApiClient;
    private final PediatricRealtimeRepository pediatricRealtimeRepository;
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

        if (responseDto.getResponse().getBody().getItems() == null) {
            System.out.println("조회 결과가 없습니다. (Empty Response)");
            return;
        }

        List<PediatricRealtimeDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        if (items == null || items.isEmpty()) {
            System.out.println("처리할 아이템이 없습니다.");
            return;
        }


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
    private Integer parseInt(String value) {
        try{
            if(value == null || value.isBlank()) return null;
            return Integer.parseInt(value);
        } catch (Exception e){
            return null;
        }
    }
    private LocalDateTime parseDateTime(String value) {
        try {
            if (value == null || value.isBlank()) return null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.parse(value, formatter);

        } catch (Exception e) {
            return null;
        }
    }
    private Boolean parseYn(String value){
        if ( value == null || value.isBlank()) return null;
        if ("Y".equalsIgnoreCase(value)) return true;
        if ("N".equalsIgnoreCase(value)) return false;
        return null;
    }
    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
