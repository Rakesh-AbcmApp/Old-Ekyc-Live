package com.kyc_routing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.kyc_routing_service")
@EnableJpaRepositories(basePackages = {
	    "com.kyc_routing_service",
	    "com.abcmkyc.repository" 
	})
	@EntityScan(basePackages = {
	    "com.kyc_routing_service",
	    "com.abcmkyc.entity" 
	})

public class KycRoutingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KycRoutingServiceApplication.class, args);
	}

}
