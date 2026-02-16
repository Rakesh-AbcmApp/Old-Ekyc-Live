package com.abcm.gst_service.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.abcm.gst_service.apicall.ServiceProviderApiCall;
import com.abcm.gst_service.createRequestBody.GstinRequestDispatcher;
import com.abcm.gst_service.dto.MerchantGstMasterRequest;
import com.abcm.gst_service.dto.MerchantResponse;
import com.abcm.gst_service.dto.ProductDetailsDto;
import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.dyanamicProviderResponse.ResponseDispatcher;
import com.abcm.gst_service.exception.CustomException;
import com.abcm.gst_service.util.CommonUtils;
import com.abcm.gst_service.util.SendFailureEmail;
import com.abcm.gst_service.util.ValidateGstnRequest;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Merchant_Master_Details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class GstLiteVerifyServiceImpl implements GstVerifyService {

	private final ServiceProviderApiCall apiCall;
	private final AsyncGstRequestSaveService asyncGstRequestSaveService;
	@Value("${merchantDetails}")
	private String merchantDetails;

	private final Environment environment;


	private final ValidateGstnRequest validateGstnRequest; 

	private final   GstinRequestDispatcher gstinRequestDispatcher;
	
	private final ResponseDispatcher dispatcher;
	
	
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
	public ResponseModel processGstLiteVerification(MerchantGstMasterRequest request, String appId, String apiKey) {
		log.info("processGstLiteVerification Invoked Method Request:{}", request.toString());
		validateGstnRequest.validateGstnRequest(request);
		log.info("After Validation for Merchant ID: {}", request.getMerchantId());
		ProductDetailsDto merchant = getProdcutDetails(request.getMerchantId());
		if(merchant == null) {
			return new ResponseModel(
					environment.getProperty("custom.messages.Not-Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Not-Found")), 
					null
					);
		}
		log.info("Merchant Valid Found to Proceed"+merchant.getProductName(),merchant.getMerchantId());
		validateGstnRequest.checkBalance(merchant.getMerchantId(),merchant.getMerchantName());
		validateGstnRequest.validateApiCredentialsAndStatus(merchant, appId, apiKey);
		Map<String , Object> requestBody=gstinRequestDispatcher.GstinProviderRequestBody(merchant.getProviderName(), request);
	    KycData kycData= asyncGstRequestSaveService.saveGstAsync(request,requestBody, merchant.getProviderName(),merchant.getProductName(),merchant.getMerchantName(),merchant.getProductRate());
	    log.info("After Saving GST Verfiy Data{}:"+kycData.getMerchantId(),kycData.getOrderId());
		return processGstResponse(requestBody, merchant,kycData.getTrackId(),kycData.getProductRate(),kycData.getOrderId());
	}

	private ResponseModel processGstResponse(Map<String, Object> requestBody, ProductDetailsDto productDetailsDto, String trackId, Long productRate,String orderId) {
	        log.info("Call  Provider Api Product GST Method Inside");
	         String response =apiCall.providerApiCall(requestBody, productDetailsDto);
	        log.info("Provider GST API Response:{}", response);
	        if ("fail:false".equalsIgnoreCase(response.trim())) {
	            log.error("API response is null or empty or Server issue");
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
	            throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
	                    Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
	        }
	        JSONObject jsonObject = new JSONObject(response);
	        ResponseModel responseModel = dispatcher.getGstinResponse(
	                productDetailsDto.getProviderName(),
	                jsonObject,
	                trackId,
	                productDetailsDto.getMerchantId(),
	                productRate,orderId);
	        return responseModel;
	    
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
	            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(MerchantResponse.class))
	            .block();

	    
	    if (response == null || response.getData() == null) {
	        log.warn("No merchant data found for MID: {}", mid);
	        throw new CustomException(environment.getProperty("custom.messages.Not-Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Data-NotFound")));
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
				.filter(product -> "GSTIN".equalsIgnoreCase(product.getProductName()))
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
									d.setGstLite(merchantMaster.getGSTIN());
									d.setProductId(product.getProductId());
									d.setProductName(product.getProductName());
									d.setProviderId(provider.getProviderId());
									d.setProviderName(provider.getProviderName());
									d.setProviderAppId(provider.getProviderAppId());
									d.setProviderAppkey(provider.getProviderAppkey());
									d.setGstliteUrl(provider.getGstinUrl());
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

	@Override
	public ResponseModel processGstAdvanceVerification(MerchantGstMasterRequest gstMasterRequest, String appId,
			String apiKey) {
		// TODO Auto-generated method stub
		return null;
	}



















}
