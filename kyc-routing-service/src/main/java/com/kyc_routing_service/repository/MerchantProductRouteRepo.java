package com.kyc_routing_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abcmkyc.entity.MerchantProductRoute;
import com.kyc_routing_service.dto.ProductRouteDTO;

public interface MerchantProductRouteRepo extends JpaRepository<MerchantProductRoute, Long> {
	
	 @Query("SELECT mpr FROM MerchantProductRoute mpr WHERE mpr.merchant.id = :merchantId")
	    List<MerchantProductRoute> findByMerchantId(@Param("merchantId") Long merchantId);
	 
	 
	 @Query("SELECT mpr.product_id AS productId, mpr.productName AS productName FROM MerchantProductRoute mpr WHERE mpr.merchant.id = :merchantId")
	 List<ProductRouteDTO> findProductRoutesByMerchantId(@Param("merchantId") Long merchantId);



}
