package com.abcm.addhar_service.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
	
	private String merchant_id;
	private String otp;
	private String consent;
	private String request_id;
	
	

} 
