package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricMkiosktyApiClient;
import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
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
    private final EmrMapper emrMapper;

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

        if (responseDto.getResponse().getBody().getItems() == null) {
            System.out.println("조회 결과가 없습니다. (Empty Response)");
            return;
        }

        List<PediatricRealtimeMkiosktyDto> items = responseDto.getResponse()
                .getBody()
                .getItems()
                .getItem();

        if (items == null || items.isEmpty()) {
            System.out.println("처리할 아이템이 없습니다.");
            return;
        }

        for (PediatricRealtimeMkiosktyDto dto : items) {
            Hospital hospital = hospitalRepository.findByHpid(dto.getHpid()).orElse(null);

            if (hospital == null) {
                System.out.println("병원 없음, skip : " + dto.getHpid());
                continue;
            }
            PediatricMkioskty entity = pediatricMkiosktyRepository.findByHospital(hospital)
                    .orElseGet(() -> emrMapper.toPediatricMkiosktyEntity(dto, hospital));

            if (entity.getId() != null) {
                emrMapper.updatePediatricMkiosktyEntity(entity, dto);
            }

            pediatricMkiosktyRepository.save(entity);
        }

    }
}
