package com.abcm.gst_service.dto;

import com.abcmkyc.entity.Merchant_Master_Details;

import lombok.Data;

@Data
public class MerchantResponse {
    private int responseCode;
    private String message;
    private Merchant_Master_Details data;
}
