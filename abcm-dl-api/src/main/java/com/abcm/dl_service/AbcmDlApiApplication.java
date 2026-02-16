package com.abcm.dl_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = {
    "com.abcm.dl_service.repository",
    "com.abcmkyc.repository" 
})
@EntityScan(basePackages = {
    "com.abcm.dl_service.entity",
    "com.abcmkyc.entity" 
})
public class AbcmDlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcmDlApiApplication.class, args);
	}

}
