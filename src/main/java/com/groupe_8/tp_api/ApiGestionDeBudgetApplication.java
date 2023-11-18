package com.groupe_8.tp_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ApiGestionDeBudgetApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGestionDeBudgetApplication.class, args);
	}
}
