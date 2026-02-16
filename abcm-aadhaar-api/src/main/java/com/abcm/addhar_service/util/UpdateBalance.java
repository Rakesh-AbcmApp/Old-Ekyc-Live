package com.abcm.addhar_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.exception.CustomException;
import com.abcm.addhar_service.repository.AadharRepository;
import com.abcmkyc.entity.Wallet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UpdateBalance {

	@Autowired
	AadharRepository aadharRepository;
	@Autowired
	Environment environment;

	public ResponseModel updateWalletBalanceAfterOtp (String merchantId, long deductionAmount) {
		log.info("Wallet balance Update Method"+merchantId);
		Wallet wallet = aadharRepository.findByMerchantId(merchantId)
				.orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.wallet-not-found"),Integer.parseInt(environment.getProperty("custom.codes.wallet-not-found"))));
		long currentBalance = wallet.getBalance();
		if (currentBalance < deductionAmount) {
			throw new CustomException(environment.getProperty("custom.messages.balance"),Integer.parseInt(environment.getProperty("custom.codes.balance")));

		}
		int rowsUpdated = aadharRepository.deductBalance(merchantId, deductionAmount);
		log.info("Balance updated for merchant {}: deducted {}, remaining {}", rowsUpdated, merchantId, deductionAmount,
				wallet.getBalance());
		return null;
	}

}
