package com.itcen.emergencyroad.external.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*
* 공공 API - 응급실 실시간 가용병상정보 조회 오퍼레이션 호출
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmrApiClient {

    private final RestTemplate restTemplate;
    private String baseUrl="https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";

    @Value("${external.emr.service-key}")
    private String serviceKey;

    public String fetchRaw(String sido, int pageNo, int numOfRows) {
        log.info("serviceKey = {}", serviceKey);
        String url = baseUrl
                + "?serviceKey=" + serviceKey
                + "&STAGE1=" + URLEncoder.encode(sido, StandardCharsets.UTF_8)
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows;
        log.info("FINAL URL ENCODED = {}", url);
        return restTemplate.getForObject(url, String.class);
    }
}