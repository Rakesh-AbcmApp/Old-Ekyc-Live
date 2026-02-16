package com.kyc_routing_service.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyc_routing_service.Entity.MerchantRoutingRequest;
import com.kyc_routing_service.Service.MerchantRoutingService;
import com.kyc_routing_service.dto.MerchantRoutingReportRequest;
import com.kyc_routing_service.dto.ProductRateRequest;
import com.kyc_routing_service.dto.ServerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/merchant-kyc-routing/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Merchant Routing", description = "APIs related to merchant routing")

@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Success",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerResponse.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerResponse.class))
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServerResponse.class))
    )
})
public class MerchantRoutingController {

	private final MerchantRoutingService merchantRoutingService;


	@PostMapping(
			value = "save",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<ServerResponse> merchnatKycRouting(@RequestBody MerchantRoutingRequest merchantRoutingRequest) {
		ServerResponse response = merchantRoutingService.SaveMerchatntRouting(merchantRoutingRequest);
		return ResponseEntity.status(response.getResponseCode()).body(response);
	}

	
	@Operation(summary = "Fetch merchant routing details", description = "Fetches routing configuration details for the given merchant ID") 
	@GetMapping("merchant-details")
	public ResponseEntity<ServerResponse>fetchMerchantDetails(@RequestParam(value = "merchantId") String merchantId)
	{
       log.info("Fetch merchant routing details{}"+merchantId);
		ServerResponse response = merchantRoutingService.FetchMerchnatRouteDetails(merchantId);
		return ResponseEntity.status(response.getResponseCode()).body(response);

	}




	@Operation(summary = "Save provider product", description = "Saves product for the provider")
    @PostMapping("save/provider/product")
	public ResponseEntity<ServerResponse>SaveProduct(@RequestParam(value = "Product") String product)
	{

		ServerResponse serverResponse= merchantRoutingService.saveProduct(product);
		return ResponseEntity.status(serverResponse.getResponseCode()).body(serverResponse);


	}

	@Operation(summary = "Save provider product rate", description = "Saves product rate for the provider")
    @PostMapping("save/provider/product/rate")
	public ResponseEntity<ServerResponse>SaveProviderProductRate()
	{

		ServerResponse serverResponse= merchantRoutingService.SaveProviderProductRate();
		return ResponseEntity.status(serverResponse.getResponseCode()).body(serverResponse);


	}

	@Operation(summary = "Fetch merchant routing report", description = "Fetches detailed merchant routing report")
    @PostMapping("fetch/merchants/routing-report")
	public ResponseEntity<ServerResponse>fetchRoutingReportDetails(@RequestBody MerchantRoutingReportRequest merchantRoutingReportRequest )
	{
		ServerResponse response=merchantRoutingService.merchantRoutingReportDetails(merchantRoutingReportRequest);
		return ResponseEntity.status(response.getResponseCode()).body(response);
	}


	@Operation(summary = "Check product rate routing", description = "Checks routing rate based on clientId, productId, and productRate")
    @PostMapping("fetch/merchants/productRate")
	public ResponseEntity<ServerResponse>fetchProductRate(@RequestBody ProductRateRequest productRateRequest )
	{
		ServerResponse response=merchantRoutingService.checkRoutingrate(productRateRequest.getClientId(),productRateRequest.getProductId(),productRateRequest.getProductRate());
		return ResponseEntity.status(response.getResponseCode()).body(response);
	}

	
	  @Operation(summary = "Get routes by MID", description = "Fetches routing details for the given merchant ID (MID)")
	    @GetMapping("fetch/routes-details")
		public ResponseEntity<ServerResponse> getRoutesByMid(@RequestParam String mid) {
		  log.info("get merchant route details by mid{}",mid);
		    ServerResponse responseDTO = merchantRoutingService.getProductRoutesByMid(mid);
		    return ResponseEntity.status(responseDTO.getResponseCode()).body(responseDTO);
		}









}
