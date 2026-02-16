package com.abcm.pan_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.abcm.pan_service.apicall.ServiceProviderApiCall;
import com.abcm.pan_service.createRequestBody.PanRequestDispatcher;
import com.abcm.pan_service.dto.MerchantResponse;
import com.abcm.pan_service.dto.PanVerifyRequest;
import com.abcm.pan_service.dto.ProductDetailsDto;
import com.abcm.pan_service.dto.ResponseModel;
import com.abcm.pan_service.dyanamicProviderResponse.ResponseDispatcher;
import com.abcm.pan_service.exception.CustomException;
import com.abcm.pan_service.util.CommonUtils;
import com.abcm.pan_service.util.SendFailureEmail;
import com.abcm.pan_service.util.ValidatePanRequest;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Merchant_Master_Details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PanVerifyServiceImpl implements PanVerifyService {

	private final ServiceProviderApiCall apiCall;
	
	private final AsyncPanRequestSaveService asyncPanRequestSaveService;
	
	@Value("${merchantDetails}")
	private String merchantDetails;
	
	private final Environment environment;
	
	private final ValidatePanRequest validatePanRequest;
	
	private final PanRequestDispatcher dispatcher;
	
	private final ResponseDispatcher responseDispatcher;
	
	
/*This Propeties to Email Trigger When Service is Down*/
	
	@Value("${email.serviceDown.subject}")
	private String serviceDownSubject;

	@Value("${email.serviceDown.template.path}")
	private String serviceDownTemplatePath;
	
	@Value("${Env}")
	private String env;

	
	private final SendFailureEmail sendFailureEmail;
	
	 @Value("${email.send.to}")
	 private String to;
	


	@Override
	public ResponseModel processPanVerification(PanVerifyRequest request, String appId, String apiKey) {
		log.info("Start process PanVerification:{processPanVerification}",request);
		
		
		validatePanRequest.validatePanRequest(request);
		
		ProductDetailsDto productDetailsDto = getProdcutDetails(request.getMerchantId());
		
		validatePanRequest.checkBalance(request.getMerchantId(),productDetailsDto.getMerchantName());
		
		validatePanRequest.validateApiCredentials(productDetailsDto, appId, apiKey);
		
		Map<String, Object> panProviderRequest = dispatcher.PanProviderRequestBody(productDetailsDto.getProviderName(), request);
		
		KycData kycData = asyncPanRequestSaveService.savePanAsync(request, panProviderRequest, productDetailsDto.getProviderName(), productDetailsDto.getProductName(), productDetailsDto.getMerchantName());
		return handlePanVerificationAsync(panProviderRequest, productDetailsDto, kycData.getTrackId(),kycData.getOrderId());
	}
	
	

	private ResponseModel handlePanVerificationAsync(Map<String, Object> panProviderRequest, ProductDetailsDto productDetailsDto, String trackId,String orderId) {
		log.info("Api handlePanVerificationAsync Call");
		String apiResponse=apiCall.providerApiCall(panProviderRequest, productDetailsDto);
		    log.info("Provider Pan Api Respopnse{}"+apiResponse);
		if (apiResponse == null || apiResponse.isBlank() || "fail:false".equalsIgnoreCase(apiResponse) || !apiResponse.trim().startsWith("{")) {
			CompletableFuture.runAsync(() -> {
				try {
					log.info("Email Template Path: {}", serviceDownTemplatePath);
					String mailstring1 = CommonUtils
							.readUsingFileInputStream(serviceDownTemplatePath);
					mailstring1 = mailstring1.replace("{{Service_Name}}", productDetailsDto.getProductName());
					mailstring1 = mailstring1.replace("{{providerName}}", productDetailsDto.getProviderName());
					mailstring1 = mailstring1.replace("{{Environment}}", env);
					mailstring1 = mailstring1.replace("{{Reason}}", "The Provider service "+ productDetailsDto.getProviderName()+" is currently unavailable. Please try again later");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
					// Get current date-time
					String timestamp = LocalDateTime.now().format(formatter);
					mailstring1 = mailstring1.replace("{{Timestamp}}", timestamp);
					sendFailureEmail.sendEkycFailureEmail(mailstring1, "", serviceDownSubject, to);
				} catch (Exception e) {
					e.printStackTrace(); // Consider logging instead
				}
			});
			log.error("API response is null, empty, 'fail:false', or not valid JSON: " + apiResponse);
			throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
					Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
		}
		JSONObject responseObj = new JSONObject(apiResponse);
		return responseDispatcher.getPanResponse(
				productDetailsDto.getProviderName(),
				responseObj,
				trackId,
				productDetailsDto.getMerchantId(),
				productDetailsDto.getProductRate(),orderId);
	}

	public Merchant_Master_Details getMerchantByMid(String mid) {
		log.info("Fetching Merchant Details for MID: {}", mid);
		WebClient webClient = WebClient.builder()
				.baseUrl(merchantDetails)
				.build();
		MerchantResponse response = webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/api/merchant-kyc-routing/merchant-details")
						.queryParam("merchantId", mid)
						.build())
				.retrieve()
				.bodyToMono(MerchantResponse.class)
				.block(); 
		if (response == null || response.getData() == null) {
			log.error("No merchant data found for MID: {}", mid);
			throw new CustomException(
					environment.getProperty("custom.messages.Not-Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Not-Found"))
					);
		}
		Merchant_Master_Details details = response.getData();
		if (details.getProductDetails() == null || details.getProductDetails().isEmpty()) {
			log.error("No product details found for merchant MID: {}", mid);
			throw new CustomException(
					environment.getProperty("custom.messages.product-not-found"),
					Integer.parseInt(environment.getProperty("custom.codes.product-not-found"))
					);
		}
		log.info("Successfully fetched Merchant Details for MID: {}", mid);
		return details;
	}

	public ProductDetailsDto extractOkycProductDetails(Merchant_Master_Details merchantMaster) {
		log.info("The merchant Active status is " + merchantMaster.isActive());
		if (merchantMaster == null || merchantMaster.getProductDetails() == null) {
			throw new RuntimeException("Merchant or Product details missing");
		}
		return merchantMaster.getProductDetails().stream()
				.filter(product -> "PAN".equalsIgnoreCase(product.getProductName()))
				.flatMap(product -> product.getProviders().stream()
						.flatMap(provider -> provider.getMerchantCharges().stream()
								.map(charges -> {
									ProductDetailsDto d = new ProductDetailsDto();
									d.setId(merchantMaster.getId());
									d.setMerchantId(merchantMaster.getMerchantId());
									d.setMerchantName(merchantMaster.getMerchantName());
									d.setAppId(merchantMaster.getAppId());
									d.setApiKey(merchantMaster.getApiKey());
									d.setActive(merchantMaster.isActive());
									d.setPAN(merchantMaster.getPAN());
									d.setProductId(product.getProductId());
									d.setProductName(product.getProductName());
									d.setProviderId(provider.getProviderId());
									d.setProviderName(provider.getProviderName());
									d.setProviderAppId(provider.getProviderAppId());
									d.setProviderAppkey(provider.getProviderAppkey());
									d.setPanUrl(provider.getPanUrl());
									d.setRouteId(charges.getRouteId());
									d.setProductRate(charges.getProductRate());
									return d;
								})
								)
						)
				.findFirst()
				.orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.Not-Found"),
						Integer.parseInt(environment.getProperty("custom.codes.Not-Found"))));
	}

	public ProductDetailsDto getProdcutDetails(String mid) {
		Merchant_Master_Details merchantMaster = getMerchantByMid(mid); // no caching
		return extractOkycProductDetails(merchantMaster);
	}
}
