package com.abcm.dl_service.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abcm.dl_service.dto.DlVerifyRequest;
import com.abcm.dl_service.dto.ProductDetailsDto;
import com.abcm.dl_service.dto.ResponseModel;
import com.abcm.dl_service.exception.CustomException;
import com.abcm.dl_service.repository.DlVerificationRepository;
import com.abcmkyc.entity.Wallet;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidatePanRequest {

	@Value("${email.lowbalance.subject}")
	private String lowbalanceSubject;

	@Value("${email.lowBalance.template.path}")
	private String emaillowBalanceTemplatePath;

	

	private final Environment environment;
	private final DlVerificationRepository dlVerificationRepository;
	private static final int MIN_REQUIRED_BALANCE = 500;
	private final SendFailureEmail sendFailureEmail;

	public void validateDlRequest(DlVerifyRequest request) {
		log.debug("Starting DL validation");
		if (isEmpty(request.getMerchantId())) {
			throw customException("merchantId-Validation");
		}
		if (!"Y".equalsIgnoreCase(request.getConsent())) {
			throw customException("Consent-Validation");
		}
		
		/*
		if(isEmpty(request.getOrderId()))
		{
			log.info("Order Id not exists");
			throw customException("order-id-missing"); 
		}
		*/
	}

	public void checkBalance(String merchantId, String MerchantName) {
		log.info("Merchant Balance check Initial Level{}" + merchantId);
		Wallet wallet = dlVerificationRepository.findByMerchantId(merchantId)
				.orElseThrow(() -> customException("Wallet-not-found"));
		LocalDate today = LocalDate.now();
		LocalDate validityDate = wallet.getValidity().toLocalDate();
		if (validityDate.isBefore(today)) {
			log.error("Wallet validity expired on {}", validityDate, "today date" + today);
			throw customException("wallet-expired");
		}
		long balance = wallet.getBalance();
		if (balance <= MIN_REQUIRED_BALANCE) {
			CompletableFuture.runAsync(() -> {
				try {
					String email = dlVerificationRepository.findEmailByMid(merchantId);
					log.info("The email Of then Merchant Id" + email);
					// Step 1: Format balance
					double finalAmount = balance / 100.0;
					String formattedAmount = String.format("%.2f", finalAmount); // → "45.00"
					log.info("Email Template Path: {}", emaillowBalanceTemplatePath);
					String mailstring1 = CommonUtils
							.readUsingFileInputStream(emaillowBalanceTemplatePath);
					mailstring1 = mailstring1.replace("{{Your_Balance}}", formattedAmount);
					mailstring1 = mailstring1.replace("{{MerchantName}}", MerchantName);
					sendFailureEmail.sendEkycFailureEmail(mailstring1, "", lowbalanceSubject, email);

				} catch (Exception e) {
					e.printStackTrace(); // Consider logging instead
				}
			});
			log.error("Insufficient balance for Merchant ID: {}. Balance: {}", merchantId, balance);
			throw customException("Balance");
		}

	}

	public void validateApiCredentials(ProductDetailsDto merchant, String appId, String apiKey) {
		log.info("Merchant validateApiCredentialscheck Initial Level{}" + merchant.getMerchantId());
		if (!Objects.equals(appId, merchant.getAppId()) || !Objects.equals(apiKey, merchant.getApiKey())) {
			log.error("Invalid API credentials for Merchant ID: {} with appId: {}", merchant.getMerchantId(), appId);
			throw customException("Key");
		}
		validateMerchantStatus(merchant);

	}

	public ResponseModel validateMerchantStatus(ProductDetailsDto merchant) {
		log.error("Merchant is inactive. Merchant ID: {}", merchant.isActive());
		if (!merchant.isActive()) {
			throw customException("merchant-inactive");
		}
		if ("DESABLE".equals(merchant.getDriving_license())) {
			log.error("DL verification service is inactive for Merchant ID: {}", merchant.getMerchantId());
			log.error("DL verification service is inactive for Merchant ID: {Status}", merchant.getDriving_license());
			throw customException("service-inactive");
		}
		log.info("Merchant status and service eligibility validated for Merchant ID: {}", merchant.getMerchantId());
		return null;
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private CustomException customException(String keyPrefix) {
		String message = environment.getProperty("custom.messages." + keyPrefix, "Unknown error");
		int code = Integer.parseInt(environment.getProperty("custom.codes." + keyPrefix, "400"));
		return new CustomException(message, code);
	}
	
	 @PostConstruct
	    public void init() {
	        System.out.println("Email Template Path: " + emaillowBalanceTemplatePath);
	    }
}
