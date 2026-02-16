package com.abcm_kyc_reporting_service.controller;

import java.util.Date;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.abcm_kyc_reporting_service.dto.KycApiCountResponseModel;
import com.abcm_kyc_reporting_service.dto.KycReportRequestModel;
import com.abcm_kyc_reporting_service.dto.ResponseModel;
import com.abcm_kyc_reporting_service.dto.StatusCheckRequest;
import com.abcm_kyc_reporting_service.service.KycReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class KycReportApiController {
	
	private final KycReportService kycReportService;
	
	Logger  log= LoggerFactory.getLogger(KycReportApiController.class);

	
	@PostMapping("/kyc/report")
	public ResponseEntity<ResponseModel> fetchOkycReport(@RequestBody KycReportRequestModel kycReportRequestModel )
	{
		try
		{
			log.info("Kyc Report Controller  Inside{}");
			ResponseModel responseModel=kycReportService.fetchReportOkyc(kycReportRequestModel);
			log.info("Return Response"+responseModel);
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);	
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
	}
	
	
	

	
	@PostMapping("/kyc/report/Merchant")
	public ResponseEntity<ResponseModel> fetchOkycReportMerchant(@RequestBody KycReportRequestModel kycReportRequestModel )
	{
		try
		{
			log.info("Kyc Report  Merchant Controller   Inside{}");
			ResponseModel responseModel=kycReportService.fetchReportOkycMerchant(kycReportRequestModel);
			log.info("Return Response"+responseModel);
			return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);	
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
	}
	
	@GetMapping("/kyc-api-count")
	public ResponseEntity<KycApiCountResponseModel> fetchKycCount(
	        @RequestParam(value = "startDate") 
	        @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @RequestParam(value = "endDate") 
	        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
	        @RequestParam(value = "merchantId", required = false, defaultValue = "") String merchantId,
	        @RequestParam(value = "product", required = false, defaultValue = "") String product) {

	    // Call the service method with these exact parameters
	    KycApiCountResponseModel response = kycReportService.fetchReportOkyc(startDate, endDate, merchantId, product);
	    
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/status-check")
	public ResponseEntity<ResponseModel>KycTxnstatusCheck( @RequestHeader("app-id") String appId,
	        @RequestHeader("api-key") String apiKey,@RequestBody StatusCheckRequest checkRequest)
	{
		ResponseModel responseModel=kycReportService.checkStatus(appId,apiKey,checkRequest);
		return ResponseEntity
	            .status(responseModel.getStatusCode())
	            .body(responseModel);
		
	}

	
	
	
}
