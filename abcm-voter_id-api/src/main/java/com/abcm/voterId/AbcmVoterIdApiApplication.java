package com.abcm.voterId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
	    "com.abcm.voterId.VoterIdRepo",
	    "com.abcmkyc.repository" 
	})
	@EntityScan(basePackages = {
	    "com.abcm.voterId.entity",
	    "com.abcmkyc.entity" 
	})
public class AbcmVoterIdApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcmVoterIdApiApplication.class, args);
	}

}
