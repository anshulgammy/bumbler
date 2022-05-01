package dev.bumbler.resilience4jwithspringboot;

import org.springframework.boot.SpringApplication;

//@SpringBootApplication
public class Resilience4jAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jAnnotationApplication.class, args);
    }

	/*@EventListener(ApplicationReadyEvent.class)
	public void runService() {
		for(int i = 1; i<12; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resilientService.runService();
		}
	}*/

}
