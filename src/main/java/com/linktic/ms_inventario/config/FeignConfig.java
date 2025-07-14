package com.linktic.ms_inventario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {

    @Value("${service.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-API-KEY", apiKey);
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
        };
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 5000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        // Use the default Feign error decoder or implement your own if needed
        return new ErrorDecoder.Default();
    }
}