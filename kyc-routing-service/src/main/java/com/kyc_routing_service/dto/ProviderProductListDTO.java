package com.kyc_routing_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@Getter
@Setter
@ToString
public class ProviderProductListDTO {

    private List<ProviderDTO> providers;
    public ProviderProductListDTO(List<ProviderDTO> providers, List<ProductRouteDTO> products) {
		super();
		this.providers = providers;
		this.products = products;
	}
	
	private List<ProductRouteDTO> products;

    


    
}
