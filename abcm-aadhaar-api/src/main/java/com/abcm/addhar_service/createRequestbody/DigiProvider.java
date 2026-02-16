package com.abcm.addhar_service.createRequestbody;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Service("digilocker")
@Slf4j
public class DigiProvider implements  ServiceProvider<Map<String, Object>, Map<String, Object>> {
	
	private static final String CONSENT_TEXT = "I hereby declare my consent agreement for fetching my information via ZOOP API.";

	@Value("${redirectUrl}")
	public String redirectUrl;

	@Value("${webhookUrl}")
	public String webhookUrl;


	@Override
	public Map<String, Object> buildSendOtpRequestBody(AadhaarOtpRequest request) {
		
		System.out.println("inside zoop provider dyanamic request");
		 Map<String, Object> data = Map.of(
		            "consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API.",
		            "customer_aadhaar_number", request.getCustomer_aadhaar_number(),
		            "consent", request.getConsent()
		        );
		        return Map.of("data", data);
	}

	@Override
	public Map<String, Object> buildVerifyOtpRequestBody(VerifyOtpRequest request,String requestId) {
		Map<String, Object> data = Map.of(
	            "consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API.",
	            "otp", request.getOtp(),
	            "consent", request.getConsent(),
	            "request_id", requestId
	        );
	        return Map.of("data", data);
	}

	@Override
	public Map<String, Object> buildDigiRequestBody(ZoopInitRequest request) {
		log.info("buildDigiRequestBody >>>>"+request.getMerchantRedirectUrl());
		
	    return Map.of(
	        "docs", List.of(request.getDocs()),  // You may want to validate or fetch dynamically
	        "purpose", "kyc",
	        "response_url", webhookUrl,
	        "redirect_url", (request.getMerchantRedirectUrl() != null && !request.getMerchantRedirectUrl().isEmpty()) 
            ? request.getMerchantRedirectUrl() 
            : redirectUrl,
            "pinless", false,
            //  "fast_track", "Y",
             // "name_to_match","",
             // "redirect_method","GET",
              "expiry_in_minutes",30
	    );
	}

	

    
}

