package com.spring.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {
    @Value("${openai.api.url}")
    private String OPENAI_API_BASE_URL;
    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(OPENAI_API_BASE_URL)
                .defaultHeader("Authorization", "Bearer " + OPENAI_API_KEY);
    }
}
