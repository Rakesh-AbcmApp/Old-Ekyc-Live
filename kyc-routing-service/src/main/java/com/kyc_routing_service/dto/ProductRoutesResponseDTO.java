package com.kyc_routing_service.dto;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Data
public class ProductRoutesResponseDTO {
	private List<RoutingDetail>routingDetails;
	
	@Data
	@Getter
	@Setter
	@ToString
    public static class RoutingDetail {
		private Long clientId;
	    private String providerName;  
	    private Long productId;
	    private String productName;  
	    private Boolean isActive;
	    private Long rate;
	    
        
    }
}
