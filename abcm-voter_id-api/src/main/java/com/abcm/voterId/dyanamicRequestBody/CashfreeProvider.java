package com.abcm.voterId.dyanamicRequestBody;


import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.voterId.DTO.VoterIdRequestModel;
import com.abcm.voterId.util.VerificationIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Service("cashfree")
@Slf4j
public class CashfreeProvider implements ServiceProvider {

	

	@Override
	public Map<String, Object> buildRequest(VoterIdRequestModel request) {
		String verificationId = VerificationIdGenerator.generateVerificationId();
	   log.info("Generated verification_id: : {} " , verificationId);
	    return Map.of(
	        "epic_number",request.getEpicNumber(),
	        "verification_id", verificationId
	    );
	   
	}
}
