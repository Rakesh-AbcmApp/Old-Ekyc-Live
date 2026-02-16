package com.abcm.dl_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abcm.dl_service.dto.DlVerifyRequest;
import com.abcm.dl_service.dto.ResponseModel;
import com.abcm.dl_service.service.DlVerifyService;

import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class DlApiController {
	
	
	@Autowired
	DlVerifyService dlVerifyService;
	
	
	@PostMapping(
	    value = "verification/driving-license",
	    consumes = MediaType.APPLICATION_JSON_VALUE,   // Accept JSON input
	    produces = MediaType.APPLICATION_JSON_VALUE    // Return JSON output
	)
	public ResponseEntity<ResponseModel> verifyPan(
	        @RequestBody DlVerifyRequest dlVerifyRequest,
	        @RequestHeader("app-id") String appId,
	        @RequestHeader("api-key") String apiKey) {
		
		log.info("Received DL verification request: {}", dlVerifyRequest);
	    ResponseModel responseModel = dlVerifyService.processDlVerification(dlVerifyRequest, appId, apiKey);
	    return ResponseEntity
	            .status(responseModel.getStatusCode())
	            .body(responseModel);
	}
	}
	
	
	
	
	
	
	
	
	
	


