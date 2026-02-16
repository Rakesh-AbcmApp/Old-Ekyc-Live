package com.abcm.kyc.service.ui.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.AppUser;



@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	Optional<AppUser> findByCredentialsUsername(String username);
	
	@Query("SELECT u.role.roleName FROM AppUser u WHERE u.credentials.username = :username")
    Optional<String> findRoleNameByUsername(String username);
    
	
}