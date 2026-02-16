package com.abcm.pan_service.dto;

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
    @JsonProperty("pan")
    private String PAN;
	private Long productId;
	private String productName;
	private Long providerId;
	private String PanUrl;
	private String providerName;
	private String providerAppId;
    private String providerAppkey;
	private Long routeId;
	private Long productRate;

}
