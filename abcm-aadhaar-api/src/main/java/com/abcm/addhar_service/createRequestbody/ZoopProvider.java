package com.abcm.addhar_service.createRequestbody;

import org.springframework.stereotype.Service;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

import java.util.Map;

@Service("zoop")
public class ZoopProvider implements ServiceProvider<Map<String, Object>, Map<String, Object>> {
	private static final String CONSENT_TEXT = "I hereby declare my consent agreement for fetching my information via ZOOP API.";


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
	public Map<String, Object> buildVerifyOtpRequestBody(VerifyOtpRequest request,String request_id) {
		Map<String, Object> data = Map.of(
	            "consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API.",
	            "otp", request.getOtp(),
	            "consent", request.getConsent(),
	            "request_id", request_id
	        );
	        return Map.of("data", data);
	}

	@Override
	public Map<String, Object> buildDigiRequestBody(ZoopInitRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

    
}

