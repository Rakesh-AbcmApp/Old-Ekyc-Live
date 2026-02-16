package com.abcm.kyc.service.ui.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abcmkyc.entity.Wallet;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
	
	
    
    
    
    //List<Wallet> findByMerchantIdAndTxnDateBetween(String merchantId, LocalDateTime startDate, LocalDateTime endDate);
	interface WalletMerchantView {
        Long          getWalletId();
        String        getMerchantId();
        String        getMerchantName();
        long          getBalance();
        long          getAlertBalance();    // ← new
        LocalDateTime getTxnDate();
    }

    @Query("""
      SELECT
        w.id             AS walletId,
        w.merchantId     AS merchantId,
        m.name           AS merchantName,
        w.balance        AS balance,
        w.alertBalance   AS alertBalance, 
        w.txnDate        AS txnDate
      FROM Wallet w
      JOIN Merchant_Master m
        ON m.mid = w.merchantId
      WHERE w.merchantId = :merchantId
        AND w.txnDate BETWEEN :startDate AND :endDate
      """)
    List<WalletMerchantView> findByMerchantIdAndTxnDateBetween(
        @Param("merchantId") String merchantId,
        @Param("startDate" ) LocalDateTime startDate,
        @Param("endDate"   ) LocalDateTime endDate
    );
    
 // Returns a single Wallet (if you expect only one)
    Optional<Wallet> findByMerchantId(String merchantId);

    // Returns a list of Wallets (if multiple records can exist)
    List<Wallet> findAllByMerchantId(String merchantId);
    
    
    Wallet getByMerchantId(String merchantId); // Single Wallet return, throws if not found
    
    
    
    //List<Wallet> findByTxnDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    
    
    @Query(value = "CALL getKycWalletSummary(?1)", nativeQuery = true)
    BigDecimal getKycWalletSummary(String merchantId);
    
    
    
    
    

    // 2) Annotate your existing method with a JPQL @Query
    @Query("""
      SELECT
        w.id             AS walletId,
        w.merchantId     AS merchantId,
        m.name           AS merchantName,
        w.balance        AS balance,
        w.alertBalance   AS alertBalance,
        w.txnDate        AS txnDate
      FROM Wallet w
      JOIN Merchant_Master m
        ON m.mid = w.merchantId
      WHERE w.txnDate BETWEEN :startDate AND :endDate
      """)
    List<WalletMerchantView> findByTxnDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate")   LocalDateTime endDate
    );
    
    
 // 2) Annotate your existing method with a JPQL @Query
    @Query("""
      SELECT
        w.id             AS walletId,
        w.merchantId     AS merchantId,
        m.name           AS merchantName,
        w.balance        AS balance,
        w.alertBalance   AS alertBalance,
        w.txnDate        AS txnDate
      FROM Wallet w
      JOIN Merchant_Master m
        ON m.mid = w.merchantId
    
      """)
    List<WalletMerchantView> findAllWalletData(
        
    );
    
    
    
    @Query("""
    	      SELECT
    	        w.id             AS walletId,
    	        w.merchantId     AS merchantId,
    	        m.name           AS merchantName,
    	        w.balance        AS balance,
    	        w.alertBalance   AS alertBalance,
    	        w.txnDate        AS txnDate
    	      FROM Wallet w, Merchant_Master m
    	      WHERE m.mid = w.merchantId
    	        AND w.merchantId = :merchantId
    	      """)
    	    List<WalletMerchantView> findAllWalletDataByMerchant(
    	        @Param("merchantId") String merchantId
    	    );
    
    
    
    @Query("""
  	      SELECT
  	        w.id             AS walletId,
  	        w.merchantId     AS merchantId,
  	        m.name           AS merchantName,
  	        w.balance        AS balance,
  	        w.alertBalance   AS alertBalance,
  	        w.txnDate        AS txnDate
  	      FROM Wallet w, Merchant_Master m
  	      WHERE m.mid = w.merchantId
  	        AND w.merchantId = :merchantId
  	      """)
  	    WalletMerchantView findAllWalletRecord(
  	        @Param("merchantId") String merchantId
  	    );
   

    
   


	


    
    
    
}
