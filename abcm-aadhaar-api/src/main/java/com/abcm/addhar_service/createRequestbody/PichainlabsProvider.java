package com.abcm.addhar_service.createRequestbody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

import lombok.extern.slf4j.Slf4j;



@Service("pichainlabs")
@Slf4j
public class PichainlabsProvider implements ServiceProvider<Map<String, Object>, Map<String, Object>> {

   
	@Override
	public Map<String, Object> buildSendOtpRequestBody(AadhaarOtpRequest request) {
		
	log.info("Inside  pichainlabs  send request body");
		return Map.of(
	            "aadhaar_no", request.getCustomer_aadhaar_number()
	            
	        );
	}

	@Override
	public Map<String, Object> buildVerifyOtpRequestBody(VerifyOtpRequest request,String reuestId) {
		log.info("Inside  pichainlabs  verify  request body");
		return Map.of(
	            "aadhaar_otp", request.getOtp(),
	            "reference_id", request.getRequest_id()
	           
	        );
	}

	@Override
	public Map<String, Object> buildDigiRequestBody(ZoopInitRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}


