package com.kyc_routing_service.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.kyc_routing_service.Service.ProviderAndProductListService;
import com.kyc_routing_service.dto.ServerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProviderAndProductListController {
	
	private final ProviderAndProductListService providerAndProductListService;
	
	
	
	
	
	
	@GetMapping("/productList")
	public ResponseEntity<ServerResponse>fetchProductList()
	{
		log.info("Product List Routing service controller inside{}");
		ServerResponse response=providerAndProductListService.productList();
		return ResponseEntity.status(response.getResponseCode()).body(response);
	}
	
	
	
	@GetMapping("/merchant/productRoutes/{merchantId}")
	public ResponseEntity<ServerResponse> fetchProductRoutesByMerchant(@PathVariable String merchantId) {
		log.info("fetchProductRoutesByMerchant Routing service controller inside{}");
	    ServerResponse response = providerAndProductListService.getProductRoutesByMerchantId(merchantId);
	    return ResponseEntity.status(response.getResponseCode()).body(response);
	}
	
	
	 

}
