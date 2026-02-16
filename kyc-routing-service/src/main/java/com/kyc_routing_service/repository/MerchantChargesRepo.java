package com.kyc_routing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.MerchantCharges;


@Repository
public interface MerchantChargesRepo extends JpaRepository<MerchantCharges, Long> {
 
}
