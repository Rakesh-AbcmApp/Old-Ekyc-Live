package com.abcm_kyc_reporting_service.dto;



import lombok.Data;

@Data
public class MerchantResponse {
    private int responseCode;
    private String message;
    private Merchant_Master data;
}
