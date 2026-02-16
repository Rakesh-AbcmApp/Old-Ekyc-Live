package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductRateRequest {
	private Long clientId;
	private Long productId;
	private Long productRate;
}
