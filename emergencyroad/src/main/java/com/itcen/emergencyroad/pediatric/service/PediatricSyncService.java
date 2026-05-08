package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
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
        String json = pediatricRealtimeStatusApiClient.getMessageRawBySido(sido, page, rows);

        PediatricRealtimeApiResponseDto responseDto = objectMapper.readValue(json, PediatricRealtimeApiResponseDto.class);

        List<PediatricRealtimeDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        for (PediatricRealtimeDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid())
                    .orElseThrow(() -> new IllegalArgumentException("병원 없음 " + dto.getHpid()));

            PediatricRealtime entity = PediatricRealtime.builder()
                    .hospital(hospital)
                    .pediatricNegativeIsolationCount(parseInt(dto.getHv15()))
                    .pediatricGeneralIsolationCount(parseInt(dto.getHv16()))
                    .pediatricBedCount(parseInt(dto.getHv28()))
                    .pediatricIcuCount(parseInt(dto.getHv32()))
                    .pediatricEmergencyIcuCount(parseInt(dto.getHv33()))
                    .pediatricEmergencyAdmissionCount(parseInt(dto.getHv37()))
                    .pediatricHotline(dto.getHv12())
                    .recordedAt(parseDateTime(dto.getHvidate()))
                    .build();

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


}
