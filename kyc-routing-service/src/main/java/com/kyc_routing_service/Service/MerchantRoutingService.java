package com.kyc_routing_service.Service;


import com.kyc_routing_service.Entity.MerchantRoutingRequest;
import com.kyc_routing_service.dto.MerchantRoutingReportRequest;
import com.kyc_routing_service.dto.ServerResponse;




public interface MerchantRoutingService {


	ServerResponse SaveMerchatntRouting (MerchantRoutingRequest merchantRoutingRequest);

	ServerResponse FetchMerchnatRouteDetails(String merchantId);
	ServerResponse saveProduct(String Product);

	ServerResponse SaveProviderProductRate();




	ServerResponse merchantRoutingReportDetails(MerchantRoutingReportRequest merchantRoutingReportRequest);
	
	ServerResponse checkRoutingrate(Long clientId, Long productId,Long productRate);
	
	
	ServerResponse getProductRoutesByMid(String mid);
}



