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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.List;

@Slf4j
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
    public void syncBySidoForPediatric(String sido) {
        int page = 1;
        int rows = 100;

        while (true) {

            String json = pediatricRealtimeStatusApiClient.getPediatricRealtimeRawBySido(sido, page, rows);

            PediatricRealtimeApiResponseDto<PediatricRealtimeDto> responseDto;

            try {
                responseDto = objectMapper.readValue(
                        json,
                        objectMapper.getTypeFactory()
                                .constructParametricType(
                                        PediatricRealtimeApiResponseDto.class,
                                        PediatricRealtimeDto.class
                                )
                );
            } catch(InvalidFormatException e){
                log.info(
                        "소아 실시간 병상 API 조회 결과 없음. items 가 빈 문자열 입니다. sido={}, page={}",
                        sido,
                        page
                );
                return;
            } catch (Exception e) {
                log.warn("소아 실시간 병상 API 응답 파싱 실패. sido={}, page={}", sido, page, e);
                return;
            }

            List<PediatricRealtimeDto> items =
                    pediatricApiResponseParser.extractItemsOrEmpty(responseDto, "소아 실시간 병상");

            if (items.isEmpty()) {
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
            Integer totalCount = responseDto.getResponse().getBody().getTotalCount();

            if (totalCount == null || page * rows >= totalCount) {
                break;
            }
            page++;
        }
    }
}
