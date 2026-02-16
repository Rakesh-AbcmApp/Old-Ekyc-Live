package com.abcmkyc.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class MerchantDetailsDto {
    
    private long id;
    private String merchantId;
    private String merchantName;
    private String appId;
    private String apiKey;
    
    @JsonProperty("okyc")
    private String OKYC;
    
    @JsonProperty("pan")
    private String PAN;
    
    @JsonProperty("gstin")
    private String GSTIN;
    
    @JsonProperty("driving_license")
    private String driving_license;
    
    
    @JsonProperty("voter_id")
    private String voter_id;
    
    
    
    private boolean isActive;
    
  
    
    List<Product> productDetails;
    
    @Data
    @ToString
    public static class Product {
        private long productId;
        private String productName;

        // Naya field for clients
        private List<Provider> Provider;
    }
    
    @Data
   
    public static class Provider {
        private long providerId;
        private String ProviderName;
        private String  aadhaarOtpSendUrl;
        private String aadhaarOtpVerifyUrl;
        private String PanUrl;
        private String GstinUrl;
        private String drivingLsUrl;
        private String voterIdUrl;
        private String providerAppId;
        private String providerAppkey;
        
        
        List<Charges> merchantcharges;
    }
    
    @Data
   
    public static class Charges {
        private long routeId;
        private String merchnatId;
       private Long productRate;
    }
}
