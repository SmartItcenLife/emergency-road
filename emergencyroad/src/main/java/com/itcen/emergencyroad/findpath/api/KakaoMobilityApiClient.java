package com.itcen.emergencyroad.findpath.api;
// 카카오 다중 목적지 길찾기 API 불러오기.
// 임포트 된 것들의 의미가 무엇인지?
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMobilityApiClient {
    private final RestTemplate restTemplate; // webClient는 비동기 통신이므로 여러 사람이 접속했을 때도 속도가 빠르다 -> web으로 구현해볼까라는 생각
    static final String baseUrl = "https://apis-navi.kakaomobility.com/v1/destinations/directions" ;

    @Value("${findpath.kakao.service-key}")
    private String KAKAO_API_KEY;

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
