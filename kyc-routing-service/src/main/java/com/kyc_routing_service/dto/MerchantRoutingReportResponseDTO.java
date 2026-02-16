package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter

public class MerchantRoutingReportResponseDTO {
	
	
	private String merchantId;
    private String merchantName;

    private Long providerId;
    private String providerName;

    private Long productId;
    private String productName;
    private double rate;
    private boolean active;
    
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    

}
