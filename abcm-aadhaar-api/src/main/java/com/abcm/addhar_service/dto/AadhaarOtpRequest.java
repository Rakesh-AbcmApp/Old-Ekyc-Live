package com.abcm.addhar_service.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AadhaarOtpRequest {
        
        private String merchant_id;       
        private String customer_aadhaar_number;
        private String consent;
        //private String OrderId;
        
    
}
