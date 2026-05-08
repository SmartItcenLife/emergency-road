package com.itcen.emergencyroad.community.service;

import com.itcen.emergencyroad.community.dto.kakao.KakaoUserInfoDto;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class KakaoService {

  private final RestTemplate restTemplate;

  public KakaoService(@Qualifier("kakaoRestTemplate") RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  @Value("${kakao.client-id}")
  private String clientId;

  @Value("${kakao.redirect-uri}")
  private String redirectUri;

  public String getKakaoLoginUrl() {
    return "https://kauth.kakao.com/oauth/authorize" + "?client_id=" + clientId
        + "&redirect_uri=" + redirectUri + "&response_type=code";
  }

  public String getAccessToken(String code){
    String url = "https://kauth.kakao.com/oauth/token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type","authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_uri", redirectUri);
    body.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
        url, HttpMethod.POST, request, Map.class
    );

    return (String) response.getBody().get("access_token");
  }

  public KakaoUserInfoDto getUserInfo(String accessToken){
    String url = "https://kapi.kakao.com/v2/user/me";

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);

    HttpEntity<Void> request = new HttpEntity<>(headers);

    ResponseEntity<KakaoUserInfoDto> response = restTemplate.exchange(
        url, HttpMethod.GET, request, KakaoUserInfoDto.class
    );
    return response.getBody();
  }
}
