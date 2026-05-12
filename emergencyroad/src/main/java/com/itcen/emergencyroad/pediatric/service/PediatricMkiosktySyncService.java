package com.itcen.emergencyroad.pediatric.service;

import com.itcen.emergencyroad.external.api.PediatricMkiosktyApiClient;
import com.itcen.emergencyroad.external.mapper.EmrMapper;
import com.itcen.emergencyroad.hospital.entity.Hospital;
import com.itcen.emergencyroad.hospital.repository.HospitalRepository;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeApiResponseDto;
import com.itcen.emergencyroad.pediatric.dto.PediatricRealtimeMkiosktyDto;
import com.itcen.emergencyroad.pediatric.entity.PediatricMkioskty;
import com.itcen.emergencyroad.pediatric.repository.PediatricMkiosktyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PediatricMkiosktySyncService {
    private final PediatricMkiosktyApiClient pediatricMkiosktyApiClient;
    private final PediatricMkiosktyRepository pediatricMkiosktyRepository;
    private final PediatricApiResponseParser pediatricApiResponseParser;
    private final HospitalRepository hospitalRepository;
    private final ObjectMapper objectMapper;
    private final EmrMapper emrMapper;

    @Transactional
    public void syncByAddrForPediatricMkioskty(String stage1, String stage2) {
        int page = 1;
        int rows = 100;

        while (true) {
            String json = pediatricMkiosktyApiClient.getPediatricMkiosktyRawByAddr(stage1, stage2, page, rows);

            PediatricRealtimeApiResponseDto<PediatricRealtimeMkiosktyDto> responseDto;

            try {
                responseDto = objectMapper.readValue(
                        json,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        PediatricRealtimeApiResponseDto.class,
                                        PediatricRealtimeMkiosktyDto.class
                                )
                );
            } catch (InvalidFormatException e) {
                log.warn(
                        "소아 중증질환 수용여부 API 조회 결과 없음. items가 빈 문자열입니다. stage1={}, stage2={}, page={}",
                        stage1,
                        stage2,
                        page
                );
                return;
            } catch (Exception e) {
                log.warn(
                        "소아 중증질환 수용여부 API 응답 파싱 실패. stage1={}, stage2={}, page={}, reason={}",
                        stage1,
                        stage2,
                        page,
                        e.getMessage()
                );
                return;
            }

            List<PediatricRealtimeMkiosktyDto> items =
                    pediatricApiResponseParser.extractItemsOrEmpty(responseDto, "소아 중증질환 수용여부");

            if (items.isEmpty()) {
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

            Integer totalCount = responseDto.getResponse().getBody().getTotalCount();
            if (totalCount == null || rows * page >= totalCount) {
                break;
            }
            page++;
        }
    }
}
