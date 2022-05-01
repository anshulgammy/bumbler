package dev.bumbler.resilience4jwithspringboot.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public final class ResilientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResilientService.class);

    private final Retry retryService;
    private final CircuitBreaker circuitBreaker;

    public ResilientService(Retry retryService, CircuitBreaker circuitBreaker) {
        this.retryService = requireNonNull(retryService);
        this.circuitBreaker = requireNonNull(circuitBreaker);
    }

    /**
     * We have configured Retry and CircuitBreaker on this method.
     * Idea is to try 'Retry' first for the configured number of times. If even after retrying this for the
     * configured number of times we continue to get exception, then CircuitBreaker will come into action.
     */
    public void runService() {
        Decorators.ofSupplier(() -> {
                    LOGGER.info("Printing from ResilientService.runService() for Decorator Implementation");
                    if (true) {
                        throw new RuntimeException("Some RuntimeException occurred!");
                    }
                    return null;
                }).withRetry(retryService)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(Arrays.asList(RuntimeException.class),
                        e -> {
                            fallbackService();
                            return null;
                        }).
                decorate().get();
    }

    private void fallbackService() {
        LOGGER.info("Printing Fallback for Resilient Service for Decorator Implementation");
    }

}
