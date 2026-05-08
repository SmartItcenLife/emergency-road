package com.itcen.emergencyroad.external.config;

import com.itcen.emergencyroad.external.api.MessageApiClient;
import com.itcen.emergencyroad.external.api.PediatricRealtimeStatusApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class PediatricApiClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public PediatricRealtimeStatusApiClient pediatricRealtimeStatusApiClient(
            RestClient restClient,
            @Value("${external.message.service-key}") String serviceKey
    ) {
        return new PediatricRealtimeStatusApiClient(restClient, serviceKey.trim());
    }
}
