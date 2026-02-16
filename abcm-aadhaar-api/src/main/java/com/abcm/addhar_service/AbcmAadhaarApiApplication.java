package com.abcm.addhar_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.abcm.addhar_service.repository",
    "com.abcmkyc.repository" 
})
@EntityScan(basePackages = {
    "com.abcm.addhar_service.entity",
    "com.abcmkyc.entity",
    
    
})
@Slf4j
public class AbcmAadhaarApiApplication {


	public static void main(String[] args) {
		log.info("Aadhaar Service start");
		SpringApplication.run(AbcmAadhaarApiApplication.class, args);
	}

}
