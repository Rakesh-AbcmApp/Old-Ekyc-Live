package com.abcm.esign_service.DTO;

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
    @JsonProperty("voter_id")
    private String voter_id;
	private Long productId;
	private String productName;
	private Long providerId;
	private String aadhaarOtpSendUrl;
	private String providerName;
	private String providerAppId;
    private String providerAppkey;
	private Long routeId;
	private Long productRate;

}
