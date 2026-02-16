package com.abcm.gst_service.createRequestBody;

import org.springframework.stereotype.Service;

import com.abcm.gst_service.dto.MerchantGstMasterRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("zoop")
public class ZoopProvider implements ServiceProvider<Map<String, Object>> {


	
	private String generateTaskId() {
		return UUID.randomUUID().toString(); // Generates a random UUID as a string
	}
	@Override
	public Map<String, Object> buildGstinRequestBody(MerchantGstMasterRequest request) {
		System.out.println("inside zoop provider dyanamic request");
		Map<String, Object> data = Map.of(
				"business_gstin_number", request.getBusinessGstinNumber(),
				"consent", request.getConsent(),
				"consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API."
				);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("mode", "sync");
		requestBody.put("task_id", generateTaskId()); // make dynamic if needed
		requestBody.put("data", data);
		return requestBody;
		 
	}
	

	

    
}

