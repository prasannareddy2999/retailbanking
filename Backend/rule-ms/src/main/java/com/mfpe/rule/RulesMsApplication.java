package com.mfpe.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RulesMsApplication {
	
	/**
	 * Main Application for Rule Microservice
	 */
	public static void main(String[] args) {
		SpringApplication.run(RulesMsApplication.class, args);
	}

}
