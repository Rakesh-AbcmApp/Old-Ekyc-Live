package com.abcm.esign_service.util;


import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.abcm.esign_service.DTO.ResponseModel;
import com.abcm.esign_service.exception.CustomException;
import com.abcm.esign_service.repo.EsignRepository;
import com.abcmkyc.entity.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateBalance {

	private final EsignRepository repository;

	private final Environment environment;

	public ResponseModel updateWalletBalance(String merchantId, long deductionAmount) {
		log.info("Wallet balance Update Merchnat ID{} " + merchantId);
		Wallet wallet =repository.findByMerchantId(merchantId)
				.orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.wallet-not-found"),
						Integer.parseInt(environment.getProperty("custom.codes.wallet-not-found"))));
		long currentBalance = wallet.getBalance();
		if (currentBalance < deductionAmount) {
			throw new CustomException(environment.getProperty("custom.messages.balance"),
					Integer.parseInt(environment.getProperty("custom.codes.balance")));
		}
		int rowsUpdated = repository.deductBalance(merchantId, deductionAmount);
		log.info("Balance updated for merchant {}: deducted {}, remaining {}", rowsUpdated, merchantId, deductionAmount,
				wallet.getBalance());
		return null;
	}

}
