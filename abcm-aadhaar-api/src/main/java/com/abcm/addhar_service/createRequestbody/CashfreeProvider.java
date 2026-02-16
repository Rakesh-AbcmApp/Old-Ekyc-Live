package com.abcm.addhar_service.createRequestbody;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service("cashfree")
public class CashfreeProvider implements ServiceProvider<Map<String, Object>, Map<String, Object>> {

   
	@Override
	public Map<String, Object> buildSendOtpRequestBody(AadhaarOtpRequest request) {
		
		System.out.println("Inside then Cashfree request body");
		return Map.of(
	          
	            "aadhaar_number", request.getCustomer_aadhaar_number()
	            
	        );
	}

	@Override
	public Map<String, Object> buildVerifyOtpRequestBody(VerifyOtpRequest request,String reuestId) {
		return Map.of(
	            "otp", request.getOtp(),
	            "ref_id", reuestId
	           
	        );
	}

	@Override
	public Map<String, Object> buildDigiRequestBody(ZoopInitRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}

