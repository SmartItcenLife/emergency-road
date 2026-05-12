package com.itcen.emergencyroad.findpath.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
/*
    https://developers.kakao.com/docs/ko/local/dev-guide
*/
public class KakaoLocalApiClient {

    private final RestTemplate restTemplate;

    @Value("${findpath.kakao.service-key}")
    private String kakaoApiKey;

    public String getDisplayLocation(Double lat, Double lon) {
        if (lat == null || lon == null) {
            return "서울특별시 중구";
        }

        try {
            return getRegionNameByCoordinate(lat, lon);
        } catch (Exception e) {
            log.warn("카카오 좌표-지역 변환 실패. lat={}, lon={}, reason={}", lat, lon, e.getMessage(), e);
            return "서울특별시 중구";
        }
    }

    private String getRegionNameByCoordinate(Double lat, Double lon){
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
                + "?x=" + lon
                + "&y=" + lat;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey); // 요청 헤더 양식

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        String responseBody = response.getBody();

        if (responseBody == null || responseBody.isBlank()){
            return "서울특별시 중구";
        }
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray documents = jsonObject.getJSONArray("documents");

        if (documents.isEmpty()){
            return "서울특별시 중구";
        }

        JSONObject region = documents.getJSONObject(0);

        String region1 = region.optString("region_1depth_name");
        String region2 = region.optString("region_2depth_name");

        return region1 + " " + region2;
    }
}
