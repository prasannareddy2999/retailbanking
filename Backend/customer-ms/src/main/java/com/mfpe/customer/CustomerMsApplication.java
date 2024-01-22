package com.mfpe.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CustomerMsApplication {
	
	/**
	 * Main Application for Customer Microservice
	 */

	public static void main(String[] args) {
		SpringApplication.run(CustomerMsApplication.class, args);
	}

}
