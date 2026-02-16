package com.abcmkyc.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Merchant_Master_Details {
    
    private long id;
    private String merchantId;
    private String merchantName;
    private String appId;
    private String apiKey;

   
    private String OKYC;

   
    private String PAN;

    
    private String GSTIN;
    
    
    private String driving_license;
    
    private String voter_id;
    
    private boolean isActive;

    @JsonProperty("productDetails")
    private List<Product> productDetails = new ArrayList<>();

    @Data
  
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Product {
        private long productId;
        private String productName;

        @JsonProperty("provider")  // fix: lowercase 'provider'
        private List<Provider> providers = new ArrayList<>();
    }

    @Data
   
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Provider {
        private long providerId;
        @JsonProperty("providerName")  // fix: lowercase 'providerName'
        private String providerName;
        private String providerAppId;
        private String providerAppkey;
        private String aadhaarOtpSendUrl;
        private String aadhaarOtpVerifyUrl;
        private String panUrl;
        private String gstinUrl;
        
        @JsonProperty("drivingLsUrl") 
        private String drivingLicenseUrl;
        
        @JsonProperty("voterIdUrl") 
        private String voterIdUrl;
        
        
        
        @JsonProperty("merchantcharges")
        private List<Charges> merchantCharges = new ArrayList<>();
    }

    @Data
  
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Charges {
        private long routeId;
        private String merchnatId;
        private Long productRate;
    }
}
