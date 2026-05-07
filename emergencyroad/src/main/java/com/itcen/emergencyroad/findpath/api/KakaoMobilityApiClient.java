package com.itcen.emergencyroad.findpath.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMobilityApiClient {
    private final RestTemplate restTemplate;
    private final String KAKAO_API_KEY = "1376420279e2ab8a126ecd931dfb8b1d";
    private final String baseUrl = "https://apis-navi.kakaomobility.com/v1/destinations/directions";

    public String fetchDirections(JSONObject requestBody){
        // 1. 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

        // 2. 요청 엔티티(헤더 + 바디) 생성
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // 3. API 호출 및 결과 반환
        log.info("카카오 길찾기 API 호출 시작");
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
        log.info("카카오 길찾기 API 호출 완료");

        return response.getBody();
    }

}
