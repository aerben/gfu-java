package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomServiceProperties.class)
public class CustomServiceAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "custom.service.enabled", havingValue = "true")
    public CustomService customService(CustomServiceProperties properties) {
        return new CustomService(properties.getMessage());
    }

}