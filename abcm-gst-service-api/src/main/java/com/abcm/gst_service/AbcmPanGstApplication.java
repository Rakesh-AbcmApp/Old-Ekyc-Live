package com.abcm.gst_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
	    "com.abcm.gst_service.repository",
	    "com.abcmkyc.repository" 
	})
	@EntityScan(basePackages = {
	    "com.abcm.gst_service.entity",
	    "com.abcmkyc.entity" 
	})
public class AbcmPanGstApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcmPanGstApplication.class, args);
	}

}
