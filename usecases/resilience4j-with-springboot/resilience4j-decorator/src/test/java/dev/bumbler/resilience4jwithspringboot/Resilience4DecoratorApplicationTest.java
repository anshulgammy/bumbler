package dev.bumbler.resilience4jwithspringboot;

import dev.bumbler.resilience4jwithspringboot.service.ResilientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class Resilience4DecoratorApplicationTest {

	@Autowired
	private ResilientService resilientService;

	@Test
	public void resilientServiceTest() {
		try {
			for (int i = 1; i < 12; i++) {
				Thread.sleep(500);
				resilientService.runService();
			}
		} catch (Exception ex) {
			fail("Exception is not expected", ex);
		}
	}

}
