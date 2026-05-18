package com.itcen.emergencyroad.external.service;

import com.itcen.emergencyroad.external.api.EmrApiClient;
import com.itcen.emergencyroad.external.dto.EmrDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

// 1. 외부 API를 page 단위로 반복 호출하여 전체 데이터 수집
// 2. JSON 응답을 파싱하여 EmrDto 리스트로 변환
// API 호출 → JSON 수신 → parseItems() → toDto() → EmrDto 리스트 반환

@Slf4j
@Service
@RequiredArgsConstructor
public class EmrSyncService {

    private final EmrApiClient apiClient;
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

    // 받아온 데이터를 dto에 담기
    private EmrDto toDto(JsonNode item) {
        return EmrDto.builder()
                // 병원 기본 정보
                .hpid(getText(item, "hpid"))
                .phpid(getText(item, "phpid"))
                .dutyName(getText(item, "dutyName"))
                .dutyTel3(getText(item, "dutyTel3"))

                //임산부 기준
                .hvs26(parseInt(getText(item, "hvs26")))
                .hvs08(parseInt(getText(item, "hvs08")))
                .hvs31(parseInt(getText(item, "hvs31")))
                .hvs32(parseInt(getText(item, "hvs32")))

                // 응급실 소아 관련 정보
                .hv11(getText(item, "hv11"))
                .hvincuayn(getText(item, "hvincuayn"))
                .hvventisoayn(getText(item, "hvventisoayn"))
                .hv42(getText(item, "hv42"))
                .hvncc(parseInt(getText(item, "hvncc")))

                // 응급실 일반 병상 정보
                .hvec(parseInt(getText(item, "hvec")))
                .hvs01(parseInt(getText(item, "hvs01")))
                .hvicc(parseInt(getText(item, "hvicc")))
                .hvs17(parseInt(getText(item, "hvs17"))) // hvicc 중복이었던 부분 수정!
                .hvcc(parseInt(getText(item, "hvcc")))
                .hvs11(getText(item, "hvs11"))
                .hvccc(parseInt(getText(item, "hvccc")))
                .hvs16(parseInt(getText(item, "hvs16")))

                // 응급실 특수 장비 여부
                .hvctayn(getText(item, "hvctayn"))     // CT
                .hvmariayn(getText(item, "hvmariayn"))   // MRI
                .hvventiayn(getText(item, "hvventiayn")) // 인공호흡기
                .hvcrrtayn(getText(item, "hvcrrtayn"))   // CRRT
                .hvecmoayn(getText(item, "hvecmoayn"))   // ECMO
                .hvangioayn(getText(item, "hvangioayn")) // 혈관조영술

                .hvidate(getText(item,"hvidate"))
                .build();
    }

    // 문자열 추출
    private String getText(JsonNode item, String fieldName) {
        JsonNode node = item.get(fieldName);
        if (node == null || node.isNull()) {
            return null;
        }
        return node.asText();
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