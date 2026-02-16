package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MerchantRoutingReportRequest {
	
	
	private String merchantId;
	private long providerId;

}
