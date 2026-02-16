package com.kyc_routing_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.Merchant_Master;


@Repository
public interface MasterMerchantRepo extends JpaRepository<Merchant_Master, Long> {
	
	
	
	Optional<Merchant_Master> findByMid(String mid);
	
	Optional<Merchant_Master> findById(long id);


	
}
