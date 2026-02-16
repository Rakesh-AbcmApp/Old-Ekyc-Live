package com.abcm.addhar_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Wallet;

import jakarta.transaction.Transactional;

@Repository
public interface AadharRepository extends JpaRepository<KycData, Long> {

	KycData findByRequestId(String request_id);

	
//	@Query("SELECT w FROM Wallet as w WHERE w.merchantId = :merchantId")
//	Optional<Wallet> findByMerchantId(String merchantId);
	
	@Query("SELECT w FROM Wallet w WHERE w.merchantId = :merchantId")
	Optional<Wallet> findByMerchantId(@Param("merchantId") String merchantId); 

	@Modifying
	@Transactional
	@Query("UPDATE Wallet w SET w.balance = w.balance - :deductAmount, w.txnDate = CURRENT_TIMESTAMP WHERE w.merchantId = :merchantId AND w.balance >= :deductAmount")
	int deductBalance(@Param("merchantId") String merchantId, @Param("deductAmount") long deductAmount);
	
	
	KycData findByTrackId(String trackId);


	@Query("SELECT w FROM KycData w WHERE w.webhookSecurityKey = :webhookSecurityKey")
	KycData findByWebhookSecurityKey(@Param("webhookSecurityKey") String webhookSecurityKey);
	
	
	
	@Query(value = "SELECT email FROM merchant_master WHERE mid = :mid", nativeQuery = true)
	String findEmailByMid(@Param("mid") String mid);
	
	
    //boolean existsByOrderId(String orderId);
	
	boolean existsByMerchantIdAndOrderId(String merchantId, String orderId);

    
  
	@Query(value = """
		    SELECT *
		    FROM abcm_kyc_ms_db.kyc_data
		    WHERE merchant_id = :merchantId
		      AND request_id  = :requestId
		    LIMIT 1
		    """, nativeQuery = true)
		Optional<KycData> findByMerchantAndRequestId(
		    @Param("merchantId") String merchantId,
		    @Param("requestId")  String requestId
		);
    
    
    
 // Repository 
    //boolean existsByOrderIdAndOtpVerifyTrue(String orderId);
    
	boolean existsByMerchantIdAndRequestIdAndOtpVerifyTrue(String merchantId, String requestId);



	
	
	
	
	


	
	

	
	
	
	


}
