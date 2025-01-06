package com.ms_cliente.ms_cliente.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfig {

    /**
     * @service cliente service
     */
    @Bean
    public WebClient webClient(final WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082/v1").build();
    }
}
