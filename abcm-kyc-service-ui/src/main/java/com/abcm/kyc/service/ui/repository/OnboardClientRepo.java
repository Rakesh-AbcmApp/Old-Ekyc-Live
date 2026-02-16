package com.abcm.kyc.service.ui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.ClientMaster;



@Repository
public interface OnboardClientRepo extends JpaRepository<ClientMaster, Long> {
	
	
	List<ClientMaster> findByStatusTrue();
	
	 ClientMaster findById(long id);
	

}
