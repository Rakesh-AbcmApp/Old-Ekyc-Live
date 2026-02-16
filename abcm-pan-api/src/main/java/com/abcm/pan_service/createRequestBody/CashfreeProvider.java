package com.abcm.pan_service.createRequestBody;


import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.pan_service.dto.PanVerifyRequest;
import com.abcm.pan_service.util.VerificationIdGenerator;

@Service("cashfree")
public class CashfreeProvider implements ServiceProvider<Map<String, Object>> {

   
	@Override
	public Map<String, Object> buildSendOtpRequestBody(PanVerifyRequest request) {
	    String verificationId = VerificationIdGenerator.generateVerificationId();
	   // System.out.println("Generated verification_id: " + verificationId);
	    return Map.of(
	        "pan",request.getCustomerPanNumber(),
	        "verification_id", verificationId
	        
	    );
	}


}

