package com.abcm.voterId.dyanamicRequestBody;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.abcm.voterId.DTO.VoterIdRequestModel;

@Service("zoop")
public class ZoopProvider implements ServiceProvider {
	private String generateTaskId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public Map<String, Object> buildRequest(VoterIdRequestModel request) {
		Map<String, Object> dataMap = Map.of(
		        "customer_epic_number", request.getEpicNumber(),
		        "consent", request.getConsent(),
		        "consent_text", "I hear by declare my consent agreement for fetching my information via ZOOP API."
		    );
		    return Map.of(
		        "data", dataMap,
		        "task_id",generateTaskId()  // assuming taskId is present in your request model
		    );
	}

	

    
}

