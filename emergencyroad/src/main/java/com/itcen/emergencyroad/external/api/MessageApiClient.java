package com.itcen.emergencyroad.external.api;

import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MessageApiClient {

    private static final String BASE_URL =
            "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmSrsillDissMsgInqire";

    private final RestClient restClient;
    private final String serviceKey;

    public MessageApiClient(RestClient restClient, String serviceKey) {
        this.restClient = restClient;
        this.serviceKey = serviceKey;
    }

    /*
        sido : 조회하고자 하는 지역 (시,도)
        page : 조회할 페이지
        rows : 조회할 데이터의 수
     */
    public String getMessagesRawBySido(String sido, int page, int rows) {
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

    public String getMessagesRawByHpid(String hpid) {
        String encodedServiceKey = URLEncoder.encode(serviceKey.trim(), StandardCharsets.UTF_8);
        String encodedHpid = URLEncoder.encode(hpid, StandardCharsets.UTF_8);

        String url = BASE_URL
                + "?serviceKey=" + encodedServiceKey
                + "&HPID=" + encodedHpid
                + "&pageNo=1"
                + "&numOfRows=10"
                + "&_type=json";

        System.out.println("serviceKey raw = " + serviceKey);
        System.out.println("요청 URL : " + url);

        return restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .body(String.class);
    }
}