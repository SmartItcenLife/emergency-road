package com.itcen.emergencyroad.external.service;

import com.itcen.emergencyroad.external.api.SrsIllApiClient;
import com.itcen.emergencyroad.external.dto.EmrDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SrsillSyncService {

    private final SrsIllApiClient  apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int NUM_OF_ROWS = 100;

    public List<EmrDto> fetchAll(String sido) {

        List<EmrDto> result = new ArrayList<>();
        int page = 1;

        while (true) {

            String json = apiClient.fetchRaw(sido, page, NUM_OF_ROWS);

            log.info("json = {}", json);

            List<EmrDto> list = parseItems(json);

            if (list.isEmpty()) break;

            result.addAll(list);

            page++;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }

    private List<EmrDto> parseItems(String json) {

        List<EmrDto> list = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);

            JsonNode itemNode = root.path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            // 단일 object or array 둘 다 대응
            if (itemNode.isMissingNode()) return list;

            if (itemNode.isArray()) {

                for (JsonNode item : itemNode) {
                    list.add(toDto(item));
                }

            } else {
                list.add(toDto(itemNode));
            }

        } catch (Exception e) {
            log.error("parse error", e);
        }

        return list;
    }

    //받아온 데이터를 dto에 담기
    private EmrDto toDto(JsonNode item) {

        return EmrDto.builder()
                .hpid(item.path("hpid").asText())
                .MKioskTy1(item.path("MKioskTy1").asText())
                .MKioskTy2(item.path("MKioskTy2").asText()) // 뇌경색
                .MKioskTy3(item.path("MKioskTy3").asText()) // 거미막하 출혈
                .MKioskTy4(item.path("MKioskTy4").asText()) // 거미막하출혈 외
                .MKioskTy5(item.path("MKioskTy5").asText()) // 대동맥응급_흉부
                .MKioskTy6(item.path("MKioskTy6").asText()) // 대동맥응급_복부
                .MKioskTy23(item.path("MKioskTy23").asText()) // 응급투석
                .MKioskTy24(item.path("MKioskTy24").asText()) // 폐쇄병동입원
                .MKioskTy11(item.path("MKioskTy11").asText()) // 응급 내시경-성인위장관
                .MKioskTy13(item.path("MKioskTy13").asText()) // 응급내시경-성인 기관지
                .MKioskTy19(item.path("MKioskTy19").asText()) // 중증화상-전문치료
                .MKioskTy26(item.path("MKioskTy26").asText()) // 영상의학혈관중재-성인

                // --- 임산부 ---
                .MKioskTy22(item.path("MKioskTy22").asText()) // 응급투석
                .MKioskTy15(item.path("MKioskTy15").asText()) // 저체중출생아 집중치료
                .MKioskTy16(item.path("MKioskTy16").asText()) // 산부인과응급 분만
                .MKioskTy17(item.path("MKioskTy17").asText()) // 산부인과응급 산과수술
                .MKioskTy18(item.path("MKioskTy18").asText()) // 산부인과응급 부인과수술

                // --- 소아 ---
                .MKioskTy10(item.path("MKioskTy10").asText()) // 장충첩/폐색_영유아
                .MKioskTy12(item.path("MKioskTy12").asText()) // 응급내시경_영유아 위장관
                .MKioskTy14(item.path("MKioskTy14").asText()) // 응급내시경_영유아_기관지
                //.MKioskTy15(item.path("MKioskTy15").asText()) // 저체중 출산아
                .MKioskTy27(item.path("MKioskTy27").asText()) // 영상의학혈관중재_영유아
                .build();
    }

    private Integer parseInt(String v) {
        try {
            if (v == null || v.isBlank()) return null;
            return Integer.parseInt(v);
        } catch (Exception e) {
            return null;
        }
    }
}
