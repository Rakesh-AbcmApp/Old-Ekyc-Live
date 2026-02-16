package com.abcmkyc.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "app_user")
@Entity
public class AppUser {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean status;
	
	
	@Embedded
	Credentials credentials;
	
	
	
	@ManyToOne
    private Role role;
}
