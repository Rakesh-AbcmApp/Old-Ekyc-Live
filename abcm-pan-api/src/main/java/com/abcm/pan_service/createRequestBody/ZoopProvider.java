package com.abcm.pan_service.createRequestBody;

import org.springframework.stereotype.Service;
import com.abcm.pan_service.dto.PanVerifyRequest;

import java.util.Map;

@Service("zoop")
public class ZoopProvider implements ServiceProvider<Map<String, Object>> {


	@Override
	public Map<String, Object> buildSendOtpRequestBody(PanVerifyRequest request) {
		
		System.out.println("inside zoop provider dyanamic request");
		 Map<String, Object> data = Map.of(
		            "consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API.",
		            "customer_pan_number", request.getCustomerPanNumber(),
		            "consent", request.getConsent()
		        );
		        return Map.of("data", data);
	}

	

    
}

