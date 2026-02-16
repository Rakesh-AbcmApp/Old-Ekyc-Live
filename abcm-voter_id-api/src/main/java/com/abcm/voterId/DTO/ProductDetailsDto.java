package com.abcm.voterId.DTO;

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
	private String voterIdUrl;
	private String providerName;
	private String providerAppId;
    private String providerAppkey;
	private Long routeId;
	private Long productRate;

}
