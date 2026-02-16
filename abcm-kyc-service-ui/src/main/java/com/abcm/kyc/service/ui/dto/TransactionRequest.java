package com.abcm.kyc.service.ui.dto;

import lombok.Data;

@Data
public class TransactionRequest{

    private String api_key;
    private String address_line_1;
    private String address_line_2;
    private String amount;
    private String city;
    private String country;
    private String currency;
    private String description;
    private String email;
    private String mode;
    private String name;
    private String order_id;
    private String phone;
    private String return_url;
    private String return_url_failure;
    private String return_url_cancel;
    private String state;
    private String payment_options;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String zip_code;
    private String hash;
    private String transaction_id;
    private String bank_code;
    private String response_code;
    
    public String getParameter(String parameter) {
        // For example, it could return a mock value, or it could retrieve data from a map or form data
        return "sampleValue"; // Mocking the retrieval for illustration purposes
    }
   
}
