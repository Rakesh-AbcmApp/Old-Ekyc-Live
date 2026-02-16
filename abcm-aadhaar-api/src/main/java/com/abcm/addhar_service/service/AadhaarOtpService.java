package com.abcm.addhar_service.service;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

public interface AadhaarOtpService {

	public ResponseModel aadharOtpRequest(AadhaarOtpRequest aadhaarOtpRequest, String appId, String apiKey);

	public ResponseModel processAadhaarOtpVerification(VerifyOtpRequest verifyOtpRequest, String appId, String apiKey);

	public ResponseModel initiateDigilocker(ZoopInitRequest request, String appId, String apiKey);

	
	

}
