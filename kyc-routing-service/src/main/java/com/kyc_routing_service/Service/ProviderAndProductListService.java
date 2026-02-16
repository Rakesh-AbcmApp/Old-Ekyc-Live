package com.kyc_routing_service.Service;

import com.kyc_routing_service.dto.ServerResponse;

public interface ProviderAndProductListService {
	
	
	
	 
	 ServerResponse fetchByProductMid(long mid);

	ServerResponse productList();

	ServerResponse getProductRoutesByMerchantId(String merchantId);
	
	

	

}
