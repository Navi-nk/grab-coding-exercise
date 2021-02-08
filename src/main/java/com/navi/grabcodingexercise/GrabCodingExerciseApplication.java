package com.navi.grabcodingexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/***
 * Main entry point to the application.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.navi.grabcodingexercise.repository")
@EnableJpaAuditing
public class GrabCodingExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrabCodingExerciseApplication.class, args);
	}

}
