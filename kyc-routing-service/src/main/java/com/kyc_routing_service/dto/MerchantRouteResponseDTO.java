package com.kyc_routing_service.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Data
@Getter
@ToString
@Setter
public class MerchantRouteResponseDTO  {
	
	private String mid;
    private String merchantName;
    private List<MerchantRoutingDTO> merchantKycRoutings;
    
    
    @Getter
    @Setter
    @Data
	public
    static  class MerchantRoutingDTO{
    private String providerName;
    private String appId;
    private String apiKey;
    private String apiAadharUrl1;
    private String apiAadharUrl2;
    private String apiPanUrl1;
    private String apiGstUrl1;
    private String productName;
    private Double merchantRate;
    private Boolean active;
    }

}
