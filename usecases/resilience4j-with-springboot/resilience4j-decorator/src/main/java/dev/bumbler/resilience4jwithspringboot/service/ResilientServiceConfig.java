package dev.bumbler.resilience4jwithspringboot.service;

import dev.bumbler.resilience4jwithspringboot.util.Constants;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilientServiceConfig {

    @Bean
    public ResilientService resilientService(
            @Qualifier(Constants.RESILIENCE_RETRY_NAME) Retry retryService,
            @Qualifier(Constants.RESILIENCE_CIRCUIT_BREAKER_NAME) CircuitBreaker circuitBreaker) {
        return new ResilientService(retryService, circuitBreaker);
    }
}
