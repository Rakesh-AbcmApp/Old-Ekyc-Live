package com.kyc_routing_service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductRouteDTO {
	private final Long productId;
    private final String productName;
}
