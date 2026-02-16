package com.abcm.pan_service.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.abcm.pan_service.dto.PanVerifyRequest;
import com.abcm.pan_service.dto.ProductDetailsDto;
import com.abcm.pan_service.dto.ResponseModel;
import com.abcm.pan_service.exception.CustomException;
import com.abcm.pan_service.repository.PanVerificationRepository;
import com.abcmkyc.entity.Wallet;
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
	
	private final SendFailureEmail sendFailureEmail;

	private final Environment environment;
	private final PanVerificationRepository verificationRepository;
	private static final int MIN_REQUIRED_BALANCE = 500;

	public void validatePanRequest(PanVerifyRequest request) {
		log.info("Starting PAN validation");
		if (isEmpty(request.getMerchantId())) {
			throw customException("merchantId-Validation");
		}
		if (!"Y".equalsIgnoreCase(request.getConsent())) {
			throw customException("Consent-Validation");
		}
		
		/*
		if (isEmpty(request.getOrderId())) {
			throw customException("order-id-missing");
		}
		*/

	}

	public void checkBalance(String merchantId, String MerchantName) {
		
		log.info("Merchant Balance check Initial Level{}" + merchantId);
		
		Wallet wallet = verificationRepository.findByMerchantId(merchantId)
				.orElseThrow(() -> customException("Wallet-not-found"));
		log.info("wallet balance before validiate pan service{}",wallet.getBalance());
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
					String SendEmailMerchant = verificationRepository.findEmailByMid(merchantId);
					log.info("Send Email Insufficient balance Pan Service" + SendEmailMerchant, balance);
					// Step 1: Format balance
					double finalAmount = balance / 100.0;
					String formattedAmount = String.format("%.2f", finalAmount); // → "45.00"
					String mailstring1 = CommonUtils.readUsingFileInputStream(emaillowBalanceTemplatePath);
					mailstring1 = mailstring1.replace("{{Your_Balance}}", formattedAmount);
					mailstring1 = mailstring1.replace("{{MerchantName}}", MerchantName);
					sendFailureEmail.sendEkycFailureEmail(mailstring1, "", lowbalanceSubject, SendEmailMerchant);
				} catch (Exception e) {
					log.error("Insufficient balance send email exception", e.getMessage());
					// e.printStackTrace(); // Consider logging instead
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

		if ("DESABLE".equals(merchant.getPAN())) {
			log.error("PAN verification service is inactive for Merchant ID: {}", merchant.getMerchantId());

			throw customException("service-inactive");
		}
		log.info("Merchant status and service eligibility validated for Merchant ID: {}", merchant.getMerchantId());
		return null;
	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty() || value=="";
	}

	private CustomException customException(String keyPrefix) {
		String message = environment.getProperty("custom.messages." + keyPrefix, "Unknown error");
		int code = Integer.parseInt(environment.getProperty("custom.codes." + keyPrefix, "400"));
		return new CustomException(message, code);
	}
}
