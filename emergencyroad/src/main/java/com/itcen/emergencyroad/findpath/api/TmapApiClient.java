package com.itcen.emergencyroad.findpath.api;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TmapApiClient {
    private final RestTemplate restTemplate;
    static final String TMAP_MATRIX_URL = "https://apis.openapi.sk.com/tmap/matrix?version=1";

    @Value("${findpath.tmap.app-key}")
    private String TMAP_APP_KEY;

    public String fetchMatrixDirections(JSONObject requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("appKey", TMAP_APP_KEY); // 카카오의 Authorization 대신 appKey 사용

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(TMAP_MATRIX_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}