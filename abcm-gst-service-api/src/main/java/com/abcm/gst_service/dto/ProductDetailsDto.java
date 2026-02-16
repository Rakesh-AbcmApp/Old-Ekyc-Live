package com.abcm.gst_service.dto;

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
    @JsonProperty("gst")
    private String gstLite;
	private Long productId;
	private String productName;
	private Long providerId;
	private String GstliteUrl;
	private String providerName;
	private String providerAppId;
    private String providerAppkey;
	private Long routeId;
	private Long productRate;

}
