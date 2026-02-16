package com.abcm.kyc.service.ui.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcm.kyc.service.ui.repository.MerchantRepository;
import com.abcm.kyc.service.ui.repository.TransactionHistoryRepo;
import com.abcm.kyc.service.ui.repository.WalletRepository;
import com.abcm.kyc.service.ui.service.WalletService;
import com.abcmkyc.entity.TransactionHistory;
import com.abcmkyc.entity.Wallet;

@Service
public class WalletServiceImpl implements WalletService{
	
	 @Autowired
	    private WalletRepository walletRepository;

	    @Autowired
	    private TransactionHistoryRepo transactionRepository;
	    
	    @Autowired
	    private MerchantRepository merchantRepository;

	@Override
	public void addBalance(String merchantId, long amount, String mode, String txnId) {

		Wallet wallet = walletRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for merchantId: " + merchantId));

        // Update wallet balance
        wallet.setBalance(wallet.getBalance()+amount);
        wallet.setTxnDate(LocalDateTime.now());

        // Create transaction
        TransactionHistory transaction = new TransactionHistory();
        transaction.setTxnId(txnId);
        transaction.setWallet(wallet);
        transaction.setMode(mode);
        transaction.setInitiateDate(LocalDateTime.now());
        transaction.setPaymentDate(LocalDateTime.now());
        transaction.setAmount(amount);
//        transaction.setRequest(requestPayload);
        transaction.setResponse("Balance added successfully");
        transaction.setTxnStatus("SUCCESS");

        // Save wallet and transaction
        
  	  walletRepository.save(wallet);

//        try {
//              merchantRepository.updateBalance(merchantId, wallet.getBalance());
//        	
//        }catch (Exception e) {
//			// TODO: handle exception\
//        	e.printStackTrace();
//		}
        transactionRepository.save(transaction);

      
    }
		
	
	

}
