package com.abcm.gst_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.exception.CustomException;
import com.abcm.gst_service.repository.GstVerificationRepository;
import com.abcmkyc.entity.Wallet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UpdateBalance {

	@Autowired
	GstVerificationRepository  gstVerificationRepository;
	@Autowired
	Environment environment;

	public ResponseModel updateWalletBalance (String merchantId, long deductionAmount) {
		log.info("Wallet balance Update Method"+merchantId);
		Wallet wallet = gstVerificationRepository.findByMerchantId(merchantId)
				.orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.wallet-not-found"),Integer.parseInt(environment.getProperty("custom.codes.wallet-not-found"))));
		long currentBalance = wallet.getBalance();
		if (currentBalance < deductionAmount) {
			throw new CustomException(environment.getProperty("custom.messages.balance"),Integer.parseInt(environment.getProperty("custom.codes.balance")));

		}
		int rowsUpdated = gstVerificationRepository.deductBalance(merchantId, deductionAmount);
		log.info("Balance updated for merchant {}: deducted {}, remaining {}", rowsUpdated, merchantId, deductionAmount,
				wallet.getBalance());
		return null;
	}

}
