package com.abcm.kyc.service.ui.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abcm.kyc.service.ui.dto.TxnReportDto;
import com.abcmkyc.entity.TransactionHistory;
import com.abcmkyc.entity.Wallet;

import jakarta.transaction.Transactional;

public interface TransactionHistoryRepo extends JpaRepository<TransactionHistory, Long> {
   @Modifying
   @Transactional
   @Query("UPDATE TransactionHistory t SET t.paymentDate = :paymentDate, t.response = :response, t.txnStatus = :txnStatus, t.mode = :mode, t.txnId = :txnId, t.wallet = :wallet WHERE t.orderId = :orderId")
   int updateTransactionByOrderIdWithWallet(LocalDateTime paymentDate, String response, String txnStatus, String mode, String txnId, Wallet wallet, String orderId);
   
   
  // List<TransactionHistory> findByWalletIdAndPaymentDateBetween(Long walletId, LocalDateTime startDate, LocalDateTime endDate);
   
   
   
   
   @Query("""
		   SELECT new com.abcm.kyc.service.ui.dto.TxnReportDto(
		     w.merchantId,
		     m.name as merchantName,
		     t.txnId as txnId ,
		     t.amount as TxnAmount,
		     t.paymentDate,
		     t.mode as TxnMode,
		     t.orderId,
		     t.txnStatus
		   )
		   FROM TransactionHistory t
		     JOIN t.wallet w
		     JOIN Merchant_Master m
		       ON m.mid = w.merchantId
		   WHERE w.id IN :walletIds
		     AND t.paymentDate BETWEEN :start AND :end
		 """)
		 List<TxnReportDto> findReportForWallets(
		     @Param("walletIds") List<Long>    walletIds,
		     @Param("start")     LocalDateTime start,
		     @Param("end")       LocalDateTime end
		 );



}