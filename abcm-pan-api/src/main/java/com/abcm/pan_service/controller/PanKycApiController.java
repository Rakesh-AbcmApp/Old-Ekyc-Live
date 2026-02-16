package com.abcm.pan_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.pan_service.dto.PanVerifyRequest;
import com.abcm.pan_service.dto.ResponseModel;
import com.abcm.pan_service.service.PanVerifyService;

import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PanKycApiController {
	
	
	@Autowired
	PanVerifyService aadhaarOtpService;
	
	
	@PostMapping(
	    value = "pan/verify-request",
	    consumes = MediaType.APPLICATION_JSON_VALUE,   // Accept JSON input
	    produces = MediaType.APPLICATION_JSON_VALUE    // Return JSON output
	)
	public ResponseEntity<ResponseModel> verifyPan(
	        @RequestBody PanVerifyRequest panVerifyRequest,
	        @RequestHeader("app-id") String appId,
	        @RequestHeader("api-key") String apiKey) {
		
		log.info("Received PAN verification request: {}", panVerifyRequest);
	    ResponseModel responseModel = aadhaarOtpService.processPanVerification(panVerifyRequest, appId, apiKey);
	    return ResponseEntity
	            .status(responseModel.getStatusCode())
	            .body(responseModel);
	}
	}
	
	
	
	
	
	
	
	
	
	


