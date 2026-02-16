package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public  class ProviderDTO {
    private Long id;
    private String serviceProviderName;
	public ProviderDTO(Long id, String serviceProviderName) {
		super();
		this.id = id;
		this.serviceProviderName = serviceProviderName;
	}
}