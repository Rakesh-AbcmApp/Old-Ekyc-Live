package com.abcm.voterId.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.abcm.voterId.DTO.ProductDetailsDto;
import com.abcm.voterId.DTO.ResponseModel;
import com.abcm.voterId.DTO.VoterIdRequestModel;
import com.abcm.voterId.VoterIdRepo.VoterIdRepository;
import com.abcm.voterId.exception.CustomException;
import com.abcmkyc.entity.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidiateVoterIdRequest {

	@Value("${email.lowbalance.subject}")
	private String lowbalanceSubject;

	@Value("${email.lowBalance.template.path}")
	private String emaillowBalanceTemplatePath;



	private final SendFailureEmail sendFailureEmail;

	private final Environment environment;
	private static final int MIN_REQUIRED_BALANCE = 500;
	private final VoterIdRepository voterIdRepository;

	public void validateVoterIdRequest(VoterIdRequestModel request) {
		log.debug("Starting  Voter Id validation",request.getMerchantId());
		
		if (isEmpty(request.getMerchantId())) {
			throw customException("merchant-id-validation");
		}
		if (!"Y".equalsIgnoreCase(request.getConsent())) {
			throw customException("consent-validation");
		}
		/*
		if (isEmpty(request.getOrderId())) {
			throw customException("order-id-missing");
		}
		*/

	}

	private boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	public void checkBalance(String merchantId, String MerchantName) {
		log.info("Merchant Balance check Initial Level{}" + merchantId);
		Wallet wallet = voterIdRepository.findByMerchantId(merchantId)
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
					String SendEmailMerchant = voterIdRepository.findEmailByMid(merchantId);
					log.info("Send Email Insufficient balance Pan Service" + SendEmailMerchant, balance);
					// Step 1: Format balance
					double finalAmount = balance / 100.0;
					String formattedAmount = String.format("%.2f", finalAmount); // → "45.00"
					String mailstring1 = CommonUtils
							.readUsingFileInputStream(emaillowBalanceTemplatePath);
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

	private CustomException customException(String keyPrefix) {
		String message = environment.getProperty("custom.messages." + keyPrefix, "Unknown error");
		int code = Integer.parseInt(environment.getProperty("custom.codes." + keyPrefix, "400"));
		return new CustomException(message, code);
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
		if ("DESABLE".equals(merchant.getVoter_id())) {
			log.error("PAN verification service is inactive for Merchant ID: {}", merchant.getMerchantId());
			throw customException("service-inactive");
		}
		log.info("Merchant status and service eligibility validated for Merchant ID: {}", merchant.getMerchantId());
		return null;
	}

}
