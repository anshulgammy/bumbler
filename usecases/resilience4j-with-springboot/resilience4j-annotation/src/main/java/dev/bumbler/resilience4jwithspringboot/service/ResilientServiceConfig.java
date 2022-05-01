package dev.bumbler.resilience4jwithspringboot.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilientServiceConfig {

    @Bean
    public ResilientService resilientService() {
        return new ResilientService();
    }
}
