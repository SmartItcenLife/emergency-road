package com.itcen.emergencyroad.external.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Slf4j
@Component
@RequiredArgsConstructor
public class SrsIllApiClient {
    private final RestTemplate restTemplate;
    private String baseUrl="https://apis.data.go.kr/B552657/ErmctInfoInqireService/getSrsillDissAceptncPosblInfoInqire";

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
