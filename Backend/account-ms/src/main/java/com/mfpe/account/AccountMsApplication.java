package com.mfpe.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountMsApplication {

	/**
	 * Main Application for Account Microservice
	 */
	public static void main(String[] args) {
		SpringApplication.run(AccountMsApplication.class, args);
	}

}
