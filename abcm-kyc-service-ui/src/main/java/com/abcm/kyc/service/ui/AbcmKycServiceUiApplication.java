package com.abcm.kyc.service.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
	    "com.abcm.kyc.service.ui",
	    "com.abcmkyc.repository" 
	})
	@EntityScan(basePackages = {
	    "com.abcm.kyc.service.ui",
	    "com.abcmkyc.entity" 
	})

public class AbcmKycServiceUiApplication {

	public static void main(String[] args) {
	    long startTime = System.currentTimeMillis(); // ⏱ Start
	    SpringApplication.run(AbcmKycServiceUiApplication.class, args);
	    long endTime = System.currentTimeMillis();   // 🛑 End
	    long startupTime = endTime - startTime;
	    System.out.println("🚀 ABCM KYC Spring Boot application started in " + startupTime + " ms");
	}

}
