package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public  class ProductDTO {
    private Long id;
    private String productName;
	public ProductDTO(Long id, String productName) {
		super();
		this.id = id;
		this.productName = productName;
	}
}