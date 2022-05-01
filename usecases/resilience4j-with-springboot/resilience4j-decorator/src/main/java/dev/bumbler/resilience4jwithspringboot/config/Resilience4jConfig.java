package dev.bumbler.resilience4jwithspringboot.config;

import dev.bumbler.resilience4jwithspringboot.util.Constants;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class Resilience4jConfig {

    @Bean
    @Qualifier(Constants.RESILIENCE_RETRY_NAME)
    public Retry resilientServiceRetry(
            @Value("${resilience4j.retry.name}") String retryName,
            @Value("${resilience4j.retry.retryService.maxRetryAttempts}") Integer maxAttempts,
            @Value("${resilience4j.retry.retryService.waitDuration}") Integer waitDurationSeconds
    ) {
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .retryExceptions(RuntimeException.class)
                .waitDuration(Duration.ofSeconds(waitDurationSeconds))
                .build();
        final RetryRegistry registry = RetryRegistry.of(config);
        Retry retry = registry.retry(retryName, config);
        return retry;
    }

    @Bean
    @Qualifier(Constants.RESILIENCE_CIRCUIT_BREAKER_NAME)
    public CircuitBreaker resilientServiceCircuitBreaker(
            @Value("${resilience4j.circuitbreaker.name}") String circuitBreakerName,
            @Value("${resilience4j.circuitbreaker.retryServiceCircuitBreaker.waitDurationInOpenState}") Integer waitDurationSeconds,
            @Value("${resilience4j.circuitbreaker.retryServiceCircuitBreaker.slidingWindowSize}") Integer slidingWindowSize,
            @Value("${resilience4j.circuitbreaker.retryServiceCircuitBreaker.failureRateThreshold}") Float failureRateThreshold,
            @Value("${resilience4j.circuitbreaker.retryServiceCircuitBreaker.permittedNumberOfCallsInHalfOpenState}") Integer permittedNumberOfCallsInHalfOpenState
    ) {
        final CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(slidingWindowSize)
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofSeconds(waitDurationSeconds))
                .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
                .build();
        final CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        final CircuitBreaker circuitBreaker = registry.circuitBreaker(circuitBreakerName);
        return circuitBreaker;
    }
}
