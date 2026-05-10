package com.itcen.emergencyroad.external.api;

import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PediatricMkiosktyApiClient {
    private static final String BASE_URL =
            "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getSrsillDissAceptncPosblInfoInqire";

    private final RestClient restClient;
    private final String serviceKey;

    public PediatricMkiosktyApiClient(RestClient restClient, String serviceKey){
        this.restClient = restClient;
        this.serviceKey = serviceKey;
    }

    public String getPediatricMkiosktyRawByAddr(String stage1, String stage2, int page, int rows ) {
        String encodedServiceKey = URLEncoder.encode(serviceKey.trim(), StandardCharsets.UTF_8);
        String encodedStage1 = URLEncoder.encode(stage1, StandardCharsets.UTF_8);
        String encodedStage2 = URLEncoder.encode(stage2, StandardCharsets.UTF_8);

        String url = BASE_URL
                + "?serviceKey=" + encodedServiceKey
                + "&STAGE1=" + encodedStage1
                + "&STAGE2=" + encodedStage2
                + "&pageNo=" + page
                + "&numOfRows=" + rows
                + "&_type=json";

        byte[] responseBytes = restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .body(byte[].class);

        String response = new String(responseBytes, StandardCharsets.UTF_8);

        return response;
    }
}
