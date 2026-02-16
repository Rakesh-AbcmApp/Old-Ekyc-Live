package com.kyc_routing_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.ProductMaster;
import com.kyc_routing_service.dto.ProductDTO;

@Repository
public interface ProductRepo extends JpaRepository<ProductMaster, Long> {
	
	
	 //Optional<ProductMaster> findByIdAndClient_Id(Long productId, Long clientId);
	
	
	@Query("SELECT new com.kyc_routing_service.dto.ProductDTO(p.id, p.productName) FROM ProductMaster p ORDER BY p.id ASC")
	List<ProductDTO> fetchProductsFast();
	   
	   
	   
	    Optional<ProductMaster> findByProductNameIgnoreCase(String productName); // Add this line

	

}
