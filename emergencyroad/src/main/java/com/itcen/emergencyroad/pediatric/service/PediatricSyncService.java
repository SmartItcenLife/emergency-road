package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeDto;
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

        List<PediatricRealtimeDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (PediatricRealtimeDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (hospital == null) {
                System.out.println("병원 없음, skip : " + dto.getHpid());
                continue;
            }
            // UPSERT
            PediatricRealtime entity = pediatricRealtimeRepository.findByHospital(hospital)
                    .orElse(
                            PediatricRealtime.builder()
                                    .hospital(hospital)
                                    .build()
                    );

            entity.updateRealtimeData(
                    dto.getHv10(),                   // 소아 인공 호흡기 가능 여부
                    dto.getHvventisoayn(),           // 조산아용 인공호흡기 가능 여부
                    dto.getHv11(),               // 인큐베이터(보육기) 가능 여부
                    dto.getHvincuayn(),              // 인큐베이터 가용 여부
                    parseInt(dto.getHv15()),         // 소아 음압격리
                    parseInt(dto.getHv16()),         // 소아 일반 격리
                    parseInt(dto.getHv28()),         // 소아 현황
                    parseInt(dto.getHv32()),         // [중환자실] 소아
                    parseInt(dto.getHv33()),         // [응급]소아 중환자실
                    parseInt(dto.getHv37()),        // [응급] 소아 입원실
                    dto.getHv12(),                   // 소아당직의 직통 연락처
                    parseDateTime(dto.getHvidate())  // 입력일시
            );

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
