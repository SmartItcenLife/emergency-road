package com.itcen.emergencyroad.external.service;

import com.itcen.emergencyroad.external.api.EgytBassInfoApiClient;
import com.itcen.emergencyroad.external.dto.EgytBassDto;
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
public class EgytBassInfoSyncService {

    private final EgytBassInfoApiClient apiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final int NUM_OF_ROWS = 1000;

    public List<EgytBassDto> fetchAll() {

        List<EgytBassDto> result = new ArrayList<>();

        int page = 1;

        while (true) {
            String json = apiClient.fetchRaw( page, NUM_OF_ROWS);

            log.info("응급의료기관 기본정보 API 응답 수신");
            List<EgytBassDto> list = parseItems(json);
            if (list.isEmpty()) {
                break;
            }
            result.addAll(list);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            break;
        }
        return result;
    }

    private List<EgytBassDto> parseItems(String json) {

        List<EgytBassDto> list = new ArrayList<>();

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
            log.error("응급의료기관 기본정보 파싱 실패", e);
        }
        return list;
    }

    // JsonNode -> DTO 변환
    private EgytBassDto toDto(JsonNode item) {

        return EgytBassDto.builder()
                .hpid(getText(item, "hpid"))
                .dutyName(getText(item, "dutyName"))
                .dutyAddr(getText(item, "dutyAddr"))
                .dutyTel1(getText(item, "dutyTel1"))
                .dutyTel3(getText(item, "dutyTel3"))
                .wgs84Lat(getDouble(item, "wgs84Lat"))
                .wgs84Lon(getDouble(item, "wgs84Lon"))
                .dgidIdName(getText(item, "dgidIdName"))
                .dutyEryn(getBoolean(item, "dutyEryn"))

                .hperyn(getInteger(item, "hperyn"))
                .hvec(getInteger(item, "hvec"))
                .hpicuyn(getInteger(item, "hpicuyn"))
                .hpnicuyn(getInteger(item, "hpnicuyn"))
                .hpopyn(getInteger(item, "hpopyn"))

                .dutyTime1s(getText(item, "dutyTime1s"))
                .dutyTime1c(getText(item, "dutyTime1c"))

                .dutyTime2s(getText(item, "dutyTime2s"))
                .dutyTime2c(getText(item, "dutyTime2c"))

                .dutyTime3s(getText(item, "dutyTime3s"))
                .dutyTime3c(getText(item, "dutyTime3c"))

                .dutyTime4s(getText(item, "dutyTime4s"))
                .dutyTime4c(getText(item, "dutyTime4c"))

                .dutyTime5s(getText(item, "dutyTime5s"))
                .dutyTime5c(getText(item, "dutyTime5c"))

                .dutyTime6s(getText(item, "dutyTime6s"))
                .dutyTime6c(getText(item, "dutyTime6c"))

                .dutyTime7s(getText(item, "dutyTime7s"))
                .dutyTime7c(getText(item, "dutyTime7c"))
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

    // Integer 추출
    private Integer getInteger(JsonNode item, String fieldName) {

        try {
            String value = getText(item, fieldName);
            if (value == null || value.isBlank()) {
                return null;
            }
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    // Double 추출
    private Double getDouble(JsonNode item, String fieldName) {

        try {
            String value = getText(item, fieldName);
            if (value == null || value.isBlank()) {
                return null;
            }
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    // Boolean 추출 (1 -> true) 2-> false
    private Boolean getBoolean(JsonNode item, String fieldName) {
        String value = getText(item, fieldName);
        if (value == null) {
            return false;
        }
        if("2".equals(value)) {
            return false;
        }
        return true;
    }
}