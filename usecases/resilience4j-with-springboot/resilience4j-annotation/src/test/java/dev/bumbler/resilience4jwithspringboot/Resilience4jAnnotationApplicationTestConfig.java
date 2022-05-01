package dev.bumbler.resilience4jwithspringboot;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan("dev.bumbler.resilience4jwithspringboot")
@SpringBootConfiguration
public class Resilience4jAnnotationApplicationTestConfig {
}
