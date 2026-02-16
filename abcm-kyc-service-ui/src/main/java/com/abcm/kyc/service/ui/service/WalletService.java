package com.abcm.kyc.service.ui.service;

import org.springframework.stereotype.Service;

public interface WalletService {

    public void addBalance(String merchantId, long amount, String mode, String txnId) ;

}
