 package com.abcm.addhar_service.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.dto.VerifyOtpRequest;
import com.abcm.addhar_service.service.AadhaarOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Slf4j
public class AadhaarKycApiController {

	private final AadhaarOtpService aadhaarOtpService;
		

	@PostMapping(value = "/aadhaar/otp/initiate", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseModel> AadharOtpRequest(@RequestBody AadhaarOtpRequest aadhaarOtpRequest,
	        @RequestHeader("app-id") String appId, @RequestHeader("api-key") String apiKey) 
	{
		  log.info("Recived aadhaar otp request: {}", aadhaarOtpRequest);
		  
	    ResponseModel response = aadhaarOtpService.aadharOtpRequest(aadhaarOtpRequest, appId, apiKey);
	    return ResponseEntity
	            .status(response.getStatusCode())  // HTTP status yahan set hota hai
	            .body(response);
	}
	
	
	
	@PostMapping(value = "aadhaar/otp/verification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseModel> AadharOtpVerify(@RequestBody VerifyOtpRequest verifyOtpRequest,
	        @RequestHeader("app-id") String appId, @RequestHeader("api-key") String apiKey) {
		log.info("Received Aadhaar OTP verification request: {}", verifyOtpRequest);
	    ResponseModel response = aadhaarOtpService.processAadhaarOtpVerification(verifyOtpRequest, appId, apiKey);
	    return ResponseEntity
	            .status(response.getStatusCode())  
	            .body(response);                   
	}
	
	//digilocker
	@PostMapping("/aadhaar/initiate")
	public ResponseEntity<ResponseModel> initDigilocker(@RequestBody  ZoopInitRequest request,
			@RequestHeader("app-id") String appId, @RequestHeader("api-key") String apiKey) {
		
		log.info("Received Digilocker initiation request: {}", request);
		ResponseModel response =aadhaarOtpService.initiateDigilocker(request,appId,apiKey);
		return ResponseEntity
	            .status(response.getStatusCode())  // HTTP status yahan set hota hai
	            .body(response);
	}
	
	
	
	

	
}
