package dev.bumbler.springreactiverestapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Playground for Mono and Flux related examples.
 *
 * This class doesn't test anything specifically.
 */
@SpringBootTest
class SpringReactiveRestApiApplicationTests {

	@Test
	public void testMono() {
		Mono<String> stringMono = Mono.just("Big Bang");
		stringMono.subscribe(System.out::println);
	}

	@Test
	public void testMonoWithLog() {
		Mono<String> stringMono = Mono.just("Big Bang").log();
		stringMono.subscribe(System.out::println);
	}

	@Test
	public void testMonoWithError() {
		Mono<String> stringMono = Mono.just("Big Bang")
				.then(Mono.error(new RuntimeException("Some error occurred")));
		stringMono.subscribe(System.out::println, error -> {
			System.out.println("Subscriber found some error: " + error.getMessage());
		});
	}

	@Test
	public void testFlux() {
		Flux<String> stringFlux = Flux.just("Alan", "Bob", "Mark");
		stringFlux.subscribe(System.out::println);
	}

	@Test
	public void testFluxWithLog() {
		Flux<String> stringFlux = Flux.just("Alan", "Bob", "Mark").log();
		stringFlux.subscribe(System.out::println);
	}

	@Test
	public void testFluxWithErrorAndLog() {
		Flux<String> stringFlux = Flux
				.just("Alan", "Bob", "Mark")
				.concatWith(Flux.error(new RuntimeException("Some Error Occurred")))
				.concatWithValues("Lucy")
				.log();
		stringFlux.subscribe(System.out::println, error -> {
			System.out.println("Subscriber has received some error: " + error.getMessage());
		});
	}
}
