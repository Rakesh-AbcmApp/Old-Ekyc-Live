package com.abcm.esign_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.abcm.esign_service.repo",   
    "com.abcmkyc.repository"         
})
@EntityScan(basePackages = {
    "com.abcmkyc.entity",           
    "com.abcm.esign_service.model"  
})
public class AbcmEsignApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbcmEsignApplication.class, args);
    }
}

