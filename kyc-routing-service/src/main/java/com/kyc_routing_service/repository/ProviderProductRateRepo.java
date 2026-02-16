package com.kyc_routing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.ProviderProductRate;

@Repository
public interface ProviderProductRateRepo extends JpaRepository<ProviderProductRate, Long>{
	
	
	
	@Query(value = "SELECT MAX(provider_rate) " +
            "FROM provider_product_rate " +
            "WHERE client_id = :clientId AND product_id = :productId", nativeQuery = true)
Long findMaxProviderRate(@Param("clientId") Long clientId,
                     @Param("productId") Long productId);

}
