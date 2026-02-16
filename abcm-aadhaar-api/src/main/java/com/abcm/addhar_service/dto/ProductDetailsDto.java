package com.abcm.addhar_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class ProductDetailsDto {

	
	private long id;
    private String merchantId;
    private String merchantName;
    private String appId;
    private String apiKey;
    private boolean isActive;
    
    @JsonProperty("okyc")
    private String OKYC;
  
	private Long productId;
	private String productName;
	
	private Long providerId;
	private String aadhaarOtpSendUrl;
	private String aadhaarOtpVerifyUrl;
	private String providerName;
	private String providerAppId;
    private String providerAppkey;
	private Long routeId;
	
	private Long productRate;
	


}
