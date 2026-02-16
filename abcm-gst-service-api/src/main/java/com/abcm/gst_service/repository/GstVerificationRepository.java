package com.abcm.gst_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abcm.gst_service.dto.MerchantGstMasterRequest;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Wallet;

import jakarta.transaction.Transactional;

@Repository
public interface GstVerificationRepository extends JpaRepository<KycData, Long> {

	Optional<KycData> findByRequestId(String request_id);

	void save(MerchantGstMasterRequest gstMasterRequest);

	@Query("SELECT w FROM Wallet w WHERE w.merchantId = :merchantId")
	Optional<Wallet> findByMerchantId(String merchantId);
	
	@Query(value = "SELECT email FROM merchant_master WHERE mid = :mid", nativeQuery = true)
	String findEmailByMid(@Param("mid") Long mid);
	
	@Modifying
	@Transactional
	@Query("UPDATE Wallet w SET w.balance = w.balance - :deductAmount, w.txnDate = CURRENT_TIMESTAMP WHERE w.merchantId = :merchantId AND w.balance >= :deductAmount")
	int deductBalance(@Param("merchantId") String merchantId, @Param("deductAmount") long deductAmount);
	
	
	@Query(value = "SELECT email FROM merchant_master WHERE mid = :mid", nativeQuery = true)
	String findEmailByMid(@Param("mid") String mid);
	
	
   // boolean existsByOrderId(String orderId);
	
	// Repository
	boolean existsByMerchantIdAndOrderId(String merchantId, String orderId);


	
	
	
	
	
	
	

	
	
	
	
	
	@Query(value = "SELECT COUNT(*) FROM role r WHERE r.role_name = :roleName", nativeQuery = true)
	int countByRoleName(@Param("roleName") String roleName);

	KycData findByTrackId(String trackId);
	
	
	
	
	
	
	
	
	
	
	
	

	

}
