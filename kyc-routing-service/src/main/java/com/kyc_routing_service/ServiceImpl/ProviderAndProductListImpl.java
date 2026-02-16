package com.kyc_routing_service.ServiceImpl;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.abcmkyc.entity.MerchantProductRoute;
import com.abcmkyc.entity.Merchant_Master;
import com.kyc_routing_service.Service.ProviderAndProductListService;
import com.kyc_routing_service.dto.ProductDTO;
import com.kyc_routing_service.dto.ProductRouteDTO;
import com.kyc_routing_service.dto.ProviderDTO;
import com.kyc_routing_service.dto.ProviderProductListDTO;
import com.kyc_routing_service.dto.ServerResponse;
import com.kyc_routing_service.repository.ClientMasterRepo;
import com.kyc_routing_service.repository.MasterMerchantRepo;
import com.kyc_routing_service.repository.MerchantProductRouteRepo;
import com.kyc_routing_service.repository.ProductRepo;
import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class ProviderAndProductListImpl  implements ProviderAndProductListService{
	private final ClientMasterRepo clientMasterRepo;
	private final ProductRepo productRepo;
	private  final MerchantProductRouteRepo merchantProductRouteRepo;
	private final MasterMerchantRepo masterMerchantRepo;

	@Override
	@Transactional(readOnly = true)
	public ServerResponse fetchByProductMid(long mid) {
		try
		{
			List<MerchantProductRoute> merchantProductRoutes=merchantProductRouteRepo.findByMerchantId(mid);
			return new ServerResponse(HttpStatus.OK.value(), "MerchantList Fetch Success", merchantProductRoutes);

		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}




	@Override
	@Transactional(readOnly = true) 
	public ServerResponse productList() {
		List<ProductDTO> products = productRepo.fetchProductsFast();
		return new ServerResponse(HttpStatus.OK.value(), "Product list fetched successfully", products);

	}



	@Override
	public ServerResponse getProductRoutesByMerchantId(String merchantId) {
		 //long startTime = System.currentTimeMillis();
		 Merchant_Master merchant = masterMerchantRepo.findByMid(merchantId).orElse(null);
	        if (merchant == null) {
	            return new ServerResponse(404, "Merchant Details not found", null);
	        }
	    List<ProviderDTO> providers = clientMasterRepo.fetchProvidersFast();
	    List<ProductRouteDTO> productRoutes = (merchantId == null)
	            ? List.of() // empty list if no merchantId
	            : merchantProductRouteRepo.findProductRoutesByMerchantId(merchant.getId());
	    String message = (merchantId == null)
	            ? "Providers fetched successfully (no products assigned or invalid merchant id)"
	            : (productRoutes.isEmpty()
	                ? "No product routes found for merchantId: " + merchantId
	                : "Product routes and providers fetched successfully");
	    ProviderProductListDTO responseDTO = new ProviderProductListDTO(providers, productRoutes);
	    int statusCode = (merchantId == null) ? 200
	                      : (productRoutes.isEmpty() ? 404 : 200);
	    
	    //long endTime = System.currentTimeMillis();
	    //long durationMs = endTime - startTime;

	    //System.out.println("getProductRoutesByMerchantId execution time: " + durationMs + " ms");

	    return new ServerResponse(statusCode, message, responseDTO);
	}




	

















}
