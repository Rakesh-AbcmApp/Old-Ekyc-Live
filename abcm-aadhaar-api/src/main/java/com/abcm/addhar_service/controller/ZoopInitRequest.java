package com.abcm.addhar_service.controller;


import lombok.Data;


@Data
public class ZoopInitRequest {
	
	private String merchant_id;   
    private String docs;
    private String consent;
    private String merchantResponseUrl;
    private String merchantRedirectUrl;
    
    private String purpose;
    private String response_url;
    private String redirect_url;
    private String fast_track;
    private boolean pinless;
    private String orderId;
  
    
    
}