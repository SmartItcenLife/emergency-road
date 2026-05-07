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

    //받아온 데이터를 dto에 담기
    private EmrDto toDto(JsonNode item) {

        return EmrDto.builder()

                .hpid(item.path("hpid").asText())
                .dutyName(item.path("dutyName").asText())
                .dutyTel3(item.path("dutyTel3").asText())
                .hv11(item.path("hv11").asText())
                .hvincuayn(item.path("hvincuayn").asText())
                .hvventisoayn(item.path("hvventisoayn").asText())
                .hv42(item.path("hv42").asText())
                .hvncc(parseInt(item.path("hvncc").asText()))
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