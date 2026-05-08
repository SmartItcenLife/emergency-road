package com.itcen.emergencyroad.external.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
 * 공공 API - 5번 응급의료기관 기본정보 조회 오퍼레이션 호출
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class EgytBassInfoApiClient {
    private final RestTemplate restTemplate;
    private final String baseUrl="https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytBassInfoInqire";

    @Value("${external.emr.service-key}")
    private String serviceKey;

    public String fetchRaw(int pageNo, int numOfRows) {
        log.info("serviceKey = {}", serviceKey);
        String url = baseUrl
                + "?serviceKey=" + serviceKey
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows;
        log.info("FINAL URL ENCODED = {}", url);
        return restTemplate.getForObject(url, String.class);
    }
}
