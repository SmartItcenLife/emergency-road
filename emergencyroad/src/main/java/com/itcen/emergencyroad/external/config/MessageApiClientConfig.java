package com.itcen.emergencyroad.external.config;

import com.itcen.emergencyroad.external.api.MessageApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MessageApiClientConfig {
        @Bean
        public RestClient restClient() {
            return RestClient.create();
        }

        @Bean
        public MessageApiClient messageOpenApiClient(
                RestClient restClient,
                @Value("${external.message.service-key}") String serviceKey
        ) {
            return new MessageApiClient(restClient, serviceKey.trim());
        }
    }
