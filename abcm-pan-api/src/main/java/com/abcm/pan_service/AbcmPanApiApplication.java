package com.abcm.pan_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = {
    "com.abcm.pan_service.repository",
    "com.abcmkyc.repository" 
})
@EntityScan(basePackages = {
    "com.abcm.pan_service.entity",
    "com.abcmkyc.entity" 
})
public class AbcmPanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcmPanApiApplication.class, args);
	}

}
