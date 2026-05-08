package com.itcen.emergencyroad.external.api;

import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class PediatricRealtimeStatusApiClient {

    private static final String BASE_URL =
            "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";

    private final RestClient restClient;
    private final String serviceKey;

    public PediatricRealtimeStatusApiClient(RestClient restClient, String serviceKey){
        this.restClient = restClient;
        this.serviceKey = serviceKey;
    }


    public String getMessageRawBySido(String sido, int page, int rows) {
        String encodedServiceKey = URLEncoder.encode(serviceKey.trim(), StandardCharsets.UTF_8);
        String encodedSido = URLEncoder.encode(sido, StandardCharsets.UTF_8);

        String url = BASE_URL
                + "?serviceKey=" + encodedServiceKey
                + "&Q0=" + encodedSido
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
