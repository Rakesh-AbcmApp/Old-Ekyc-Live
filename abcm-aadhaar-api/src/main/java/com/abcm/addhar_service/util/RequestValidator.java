package com.abcm.addhar_service.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abcm.addhar_service.Email.CommonUtils;
import com.abcm.addhar_service.Email.SendFailureEmail;
import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.ProductDetailsDto;
import com.abcm.addhar_service.dto.VerifyOtpRequest;
import com.abcm.addhar_service.exception.CustomException;
import com.abcm.addhar_service.repository.AadharRepository;
import com.abcmkyc.entity.Wallet;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestValidator {

    @Value("${email.lowbalance.subject}")
    private String lowBalanceSubject;

    @Value("${email.lowBalance.template.path}")
    private String lowBalanceTemplatePath;

    private static final int MIN_REQUIRED_BALANCE = 500;

    private final Environment env;
    private final AadharRepository aadharRepository;
    private final SendFailureEmail sendFailureEmail;

    public RequestValidator(Environment env, AadharRepository aadharRepository, SendFailureEmail sendFailureEmail) {
        this.env = env;
        this.aadharRepository = aadharRepository;
        this.sendFailureEmail = sendFailureEmail;
    }

    

    public void validateAadhaarOtpRequest(AadhaarOtpRequest request) {
        log.info("Validating Aadhaar OTP Request");
        validateMerchantId(request.getMerchant_id());
        validateConsent(request.getConsent());
        validateAadhaarNumber(request.getCustomer_aadhaar_number());
       // validateorderID(request.getOrderId());
    }

    public void validateVerifyOtpRequest(VerifyOtpRequest request) {
        log.info("Validating Verify OTP Request");
        validateMerchantId(request.getMerchant_id());
        validateOtp(request.getOtp());
        validateConsent(request.getConsent());
        validateRequestId(request.getRequest_id());
    }

    public void validateAadhaardigiRequest(ZoopInitRequest request) {
        log.info("Validating Zoop Aadhaar Digi Request");
        validateMerchantId(request.getMerchant_id());
        validateConsent(request.getConsent());
        validateAadhaarDoc(request.getDocs());
        validateResponseUrl(request.getMerchantResponseUrl());
        validateorderID(request.getOrderId());
    }

    public void validateMerchantBalance(String merchantId, String merchantName) {
        log.info("Validating balance for merchant: {}", merchantId);

        Wallet wallet = aadharRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> throwValidationError("wallet-not-found"));

        long balance = wallet.getBalance();
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = wallet.getValidity().toLocalDate();

        if (expiryDate.isBefore(today)) {
            throw throwValidationError("wallet-expired");
        }

        if (balance <= MIN_REQUIRED_BALANCE) {
            sendLowBalanceEmailAsync(merchantId, merchantName, balance);
            throw throwValidationError("balance");
        }
    }

    public void validateApiCredentialsAndStatus(ProductDetailsDto merchant, String appId, String apiKey) {
        if (!Objects.equals(appId, merchant.getAppId()) || !Objects.equals(apiKey, merchant.getApiKey())) {
            throw throwValidationError("key");
        }

        if (!merchant.isActive()) {
            log.info("Merchant is inactive");
            throw throwValidationError("merchant-inactive");
        }

        if ("DESABLE".equalsIgnoreCase(merchant.getOKYC())) {
            throw throwValidationError("service-inactive");
        }
    }

    // --------------------- Private Validators ---------------------

    private void validateMerchantId(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            throw throwValidationError("merchant-id-validation");
        }
    }

    private void validateConsent(String consent) {
        if (!"Y".equalsIgnoreCase(consent)) {
            throw throwValidationError("consent-validation");
        }
    }
    
    private  void validateorderID(String orderId)
    {
    	if (orderId == null || orderId.isBlank() ) {
            throw throwValidationError("order-id-missing");
        }
    }

    private void validateAadhaarNumber(String aadhaar) {
        if (aadhaar == null || aadhaar.isBlank() || !aadhaar.trim().matches("\\d{12}")) {
            throw throwValidationError("aadhar-no-formate-validation");
        }
    }

    private void validateOtp(String otp) {
        if (otp == null || otp.isBlank()) {
            throw throwValidationError("aadhaar-otp-validation");
        }
    }

    private void validateRequestId(String requestid) {
        if (requestid == null || requestid.isBlank()) {
            throw throwValidationError("request-id-validation");
        }
    }

    private void validateResponseUrl(String responseUrl) {
        if (responseUrl == null || responseUrl.isBlank()) {
            throw throwValidationError("response-validation");
        }
    }

    private void validateAadhaarDoc(String docType) {
        if (!"ADHAR".equalsIgnoreCase(docType)) {
            throw throwValidationError("doc-validation");
        }
    }

    // --------------------- Utility Methods ---------------------

    private void sendLowBalanceEmailAsync(String merchantId, String merchantName, long balanceInPaise) {
        CompletableFuture.runAsync(() -> {
            try {
                String email = aadharRepository.findEmailByMid(merchantId);
                double balance = balanceInPaise / 100.0;

                String content = CommonUtils.readUsingFileInputStream(lowBalanceTemplatePath)
                        .replace("{{Your_Balance}}", String.format("%.2f", balance))
                        .replace("{{MerchantName}}", merchantName);

                sendFailureEmail.sendAadhaarFailureEmail(content, "", lowBalanceSubject, email);
                log.info("Low balance email sent to {}", email);
            } catch (Exception e) {
                log.error("Error sending low balance email", e);
            }
        });
    }

    private CustomException throwValidationError(String key) {
        return new CustomException(
                env.getProperty("custom.messages." + key),
                Integer.parseInt(env.getProperty("custom.codes." + key))
        );
    }
}
