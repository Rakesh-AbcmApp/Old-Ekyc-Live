package com.kyc_routing_service.Entity;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class MerchantRoutingRequest {
	private String merchantId;
	private List<RoutingDetail>routingDetails;
	
	@Data
	@Getter
	@Setter
	@ToString
    public static class RoutingDetail {
        private Long clientId;
        private Long productId;
        private double rate;
        private String createdBy; 
        private String updatedby;
        
    }


}
