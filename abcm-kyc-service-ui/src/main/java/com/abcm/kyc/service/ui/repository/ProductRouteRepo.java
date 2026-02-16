package com.abcm.kyc.service.ui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.MerchantProductRoute;


@Repository
public interface ProductRouteRepo extends JpaRepository<MerchantProductRoute, Long> {
	
	@Query(value = "SELECT COUNT(*) FROM merchant_product_route WHERE mid = :mid", nativeQuery = true)
	Long getProductRouteCountByMid(@Param("mid") String mid);
	
	
	@Query(value = "SELECT COUNT(product_id) FROM product_master", nativeQuery = true)
    Long getTotalProductCount();


}
