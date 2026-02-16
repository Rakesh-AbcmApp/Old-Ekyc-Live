package com.abcm.gst_service.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.abcm.gst_service.dto.MerchantGstMasterRequest;
import com.abcm.gst_service.dto.ProductDetailsDto;
import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.exception.CustomException;
import com.abcm.gst_service.repository.GstVerificationRepository;
import com.abcmkyc.entity.Wallet;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidateGstnRequest {

    private final Environment environment;
    private final GstVerificationRepository gstVerificationRepository;
    private static final int MIN_REQUIRED_BALANCE = 500;
    
    @Value("${email.lowbalance.subject}")
	private String lowbalanceSubject;

	
	
	
	@Value("${email.lowBalance.template.path}")
	private String emaillowBalanceTemplatePath;
	
    private final SendFailureEmail sendFailureEmail;
    
  

    public ValidateGstnRequest(Environment environment, GstVerificationRepository gstVerificationRepository,SendFailureEmail sendFailureEmail) {
        this.environment = environment;
        this.gstVerificationRepository = gstVerificationRepository;
        this.sendFailureEmail=sendFailureEmail;
    }

    public ResponseModel validateGstnRequest(MerchantGstMasterRequest request) {
        log.info("Validating GSTN request for merchant: {}", request.getMerchantId());
        try {
            validateMerchantId(request.getMerchantId());
            validateGstinNumber(request.getBusinessGstinNumber());
            validateConsent(request.getConsent());
           // validateOrderID(request.getOrderId());
            return buildSuccessResponse();
        } catch (CustomException e) {
           // log.error("Validation failed: {}", e.getMessage());
            throw e;
        }
    }

    /*
    private void validateOrderID(String orderId) {
    	 if (isNullOrEmpty(orderId)) {
             throw customException("order-id-missing");
         }
		
	}
*/
	private void validateMerchantId(String merchantId) {
        if (isNullOrEmpty(merchantId)) {
            throw customException("merchant-id-validation");
        }
    }

    private void validateGstinNumber(String gstinNumber) {
        if (isNullOrEmpty(gstinNumber)) {
            throw customException("gst-validation");
        }
    }

    private void validateConsent(String consent) {
        if (!"Y".equalsIgnoreCase(consent)) {
            throw customException("consent-validation");
        }
    }

    public void checkBalance(String merchantId,String MerchantName) {
        Wallet wallet = gstVerificationRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> customException("wallet-not-found"));
        LocalDate today = LocalDate.now();  
        LocalDate validityDate = wallet.getValidity().toLocalDate();  
        if (validityDate.isBefore(today)) {
            log.error("Wallet validity expired on {}", validityDate,"today date"+today);
            throw customException("wallet-expired");
        } 
        long balance = wallet.getBalance();
        if (balance <= MIN_REQUIRED_BALANCE) {
        	
        	CompletableFuture.runAsync(() -> {
                try {
                	String SendEmailMerchant = gstVerificationRepository.findEmailByMid(merchantId);
                	log.info("The email Of then Merchant Id"+SendEmailMerchant);
                    // Step 1: Format balance
                	double finalAmount = balance / 100.0;
            		String formattedAmount = String.format("%.2f", finalAmount); // → "45.00"
            		String mailstring1 = CommonUtils.readUsingFileInputStream(emaillowBalanceTemplatePath);
                    mailstring1 = mailstring1.replace("{{Your_Balance}}",formattedAmount);
                    mailstring1 = mailstring1.replace("{{MerchantName}}", MerchantName);
                    sendFailureEmail.sendEkycFailureEmail(mailstring1, "", lowbalanceSubject,SendEmailMerchant);

                } catch (Exception e) {
                    e.printStackTrace(); // Consider logging instead
                }
            });
        	
        	
            log.error("Insufficient balance for merchant ID: {}. Balance: {}", merchantId, balance);
            throw customException("balance");
        }
    }

    public void validateApiCredentialsAndStatus(ProductDetailsDto merchant, String appId, String apiKey) {
        if (!Objects.equals(appId, merchant.getAppId()) || !Objects.equals(apiKey, merchant.getApiKey())) {
            throw customException("key");
        }

        if (!merchant.isActive()) {
            throw customException("merchant-inactive");
        }

        if ("DESABLE".equalsIgnoreCase(merchant.getGstLite())) {
            throw customException("service-inactive");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private ResponseModel buildSuccessResponse() {
        return new ResponseModel(
                environment.getProperty("custom.messages.success", "Validation successful"),
                Integer.parseInt(environment.getProperty("custom.codes.success", "200")),
                true
        );
    }

    private CustomException customException(String keyPrefix) {
        String messageKey = "custom.messages." + keyPrefix;
        String codeKey = "custom.codes." + keyPrefix;

        String message = environment.getProperty(messageKey, "Unknown error");
        int code = Integer.parseInt(environment.getProperty(codeKey, "400"));

        return new CustomException(message, code);
    }
}
