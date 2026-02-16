package com.abcm.kyc.service.ui.dto;

import lombok.Data;

@Data
public class PaymentResponseModel {
	
	private String transaction_id;
    private String country;
    private String amount;
    private String payment_mode;
    private String response_code;
    private String city;
    private String description;
    private String udf3;
    private String udf4;
    private String udf5;
    private String udf1;
    private String udf2;
    private String zip_code;
    private String response_message;
    private String valid_hash;
    private String payment_channel;
    private String phone;
    private String calculatedHash;
    private String payment_datetime;
    private String name;
    private String currency;
    private String state;
    private String order_id;
    private String email;
}

