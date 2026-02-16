package com.abcm.voterId.VoterIdRepo;

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
public interface VoterIdRepository extends JpaRepository<KycData, Long> {


	Optional<KycData> findByRequestId(String request_id);

	//@Query("SELECT w FROM Wallet w WHERE w.merchantId = :merchantId")
	
	@Query(value ="SELECT * FROM wallet WHERE merchant_id = ?1" , nativeQuery = true)
	Optional<Wallet> findByMerchantId(String merchantId);

	@Modifying
	@Transactional
	@Query("UPDATE Wallet w SET w.balance = w.balance - :deductAmount, w.txnDate = CURRENT_TIMESTAMP WHERE w.merchantId = :merchantId AND w.balance >= :deductAmount")
	int deductBalance(@Param("merchantId") String merchantId, @Param("deductAmount") long deductAmount);

	KycData findByTrackId(String trackId);

	@Query(value = "SELECT email FROM merchant_master WHERE mid = :mid", nativeQuery = true)
	String findEmailByMid(@Param("mid") String mid);
	
	
    //boolean existsByOrderId(String orderId);
	
	boolean existsByMerchantIdAndOrderId(String merchantId, String orderId);


}
