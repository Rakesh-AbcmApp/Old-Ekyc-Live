package com.abcm.gst_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.dto.MerchantGstMasterRequest;
import com.abcm.gst_service.service.GstVerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class GstApiController {
	
	private final GstVerifyService  gstVerifyService;
	
	@PostMapping("/gst/verify")
	public ResponseEntity<ResponseModel>verifyGstLite(@RequestBody MerchantGstMasterRequest  gstMasterRequest, @RequestHeader("app-id") String appId,
	        @RequestHeader("api-key") String apiKey)
	{
		log.info("Received GST verification request: {}", gstMasterRequest);
		
		return new ResponseEntity<ResponseModel>(gstVerifyService.processGstLiteVerification(gstMasterRequest, appId, apiKey),HttpStatus.OK);
	}
	
	
	@PostMapping("/gst/verify-advance")
	public ResponseEntity<ResponseModel>verifyGstAdvance( @RequestBody MerchantGstMasterRequest  gstMasterRequest, @RequestHeader("app-id") String appId,
	        @RequestHeader("api-key") String apiKey)
	{
		return new ResponseEntity<ResponseModel>(gstVerifyService.processGstAdvanceVerification(gstMasterRequest, appId, apiKey),HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
