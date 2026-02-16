package com.abcm_kyc_reporting_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.abcm_kyc_reporting_service.repository",
    "com.abcmkyc.repository" 
})
@EntityScan(basePackages = {
    "com.abcm_kyc_reporting_service.entity", // Typically entities are in entity package
    "com.abcmkyc.entity" // Scan the entire entity package
})
public class AbcmKycReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbcmKycReportingServiceApplication.class, args);
    }
}