package com.itcen.emergencyroad.external.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
/*
*
1. URL 자동 인코딩 막기
2. 이미 만든 URL 그대로 전송
3. 공공 API 깨지는 문제 방지
* */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 1. UriBuilderFactory 생성
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

        // 2. 인코딩 모드를 NONE으로 설정 (이미 인코딩된 URL을 그대로 사용하기 위함)
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // 3. RestTemplate에 설정 적용
        restTemplate.setUriTemplateHandler(factory);
        return restTemplate;
    }

    @Bean
    @Qualifier("kakaoRestTemplate")
    public RestTemplate kakaoRestTemplate() {
        return new RestTemplate();
    }
}
