package com.kyc_routing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.Wallet;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long> {

}
