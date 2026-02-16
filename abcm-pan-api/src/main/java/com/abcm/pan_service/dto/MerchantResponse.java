package com.abcm.pan_service.dto;

import com.abcmkyc.entity.Merchant_Master_Details;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class MerchantResponse {
	private int responseCode;
    @JsonAlias({"message", "massage"})
    private String message;
    private Merchant_Master_Details data;
}
