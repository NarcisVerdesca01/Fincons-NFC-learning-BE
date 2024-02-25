package com.fincons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware") // nome del bean nella SecurityConfiguration
public class FinconsNfcLearningBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinconsNfcLearningBeApplication.class, args);
	}



}
