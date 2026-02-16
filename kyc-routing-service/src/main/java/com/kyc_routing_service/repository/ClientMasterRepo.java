package com.kyc_routing_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.ClientMaster;
import com.kyc_routing_service.dto.ProviderDTO;

@Repository
public interface ClientMasterRepo extends JpaRepository<ClientMaster,Long> {
	
	
	
	@Query("SELECT new com.kyc_routing_service.dto.ProviderDTO(c.id, c.serviceProviderName) FROM ClientMaster c")
    List<ProviderDTO> fetchProvidersFast();

	
	

}
