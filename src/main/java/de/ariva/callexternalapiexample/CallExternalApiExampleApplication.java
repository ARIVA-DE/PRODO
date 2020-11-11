package de.ariva.callexternalapiexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CallExternalApiExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CallExternalApiExampleApplication.class, args);
	}

}
