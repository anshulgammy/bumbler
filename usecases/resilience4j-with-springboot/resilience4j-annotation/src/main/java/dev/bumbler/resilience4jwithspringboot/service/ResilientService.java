package dev.bumbler.resilience4jwithspringboot.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResilientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResilientService.class);

    /**
     * We have configured Retry and CircuitBreaker on this method.
     * Idea is to try 'Retry' first for the configured number of times. If even after retrying this for the
     * configured number of times we continue to get exception, then CircuitBreaker will come into action.
     */
    @CircuitBreaker(name = "serviceCircuitBreaker", fallbackMethod = "fallbackService")
    @Retry(name = "retryService")
    public void runService() {
        LOGGER.info("Printing from ResilientService.runService()");
        throw new RuntimeException("Some RuntimeException occurred!");
    }

    private void fallbackService(RuntimeException runtimeException) {
        LOGGER.info("Printing Fallback for Resilient Service");
    }

}
