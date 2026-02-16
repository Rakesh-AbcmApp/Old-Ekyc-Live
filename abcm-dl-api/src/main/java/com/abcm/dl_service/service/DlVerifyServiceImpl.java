package com.abcm.dl_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.abcm.dl_service.apicall.ServiceProviderApiCall;
import com.abcm.dl_service.createRequestBody.DlRequestDispatcher;
import com.abcm.dl_service.dto.MerchantResponse;
import com.abcm.dl_service.dto.DlVerifyRequest;
import com.abcm.dl_service.dto.ProductDetailsDto;
import com.abcm.dl_service.dto.ResponseModel;
import com.abcm.dl_service.dyanamicProviderResponse.ResponseDispatcher;
import com.abcm.dl_service.exception.CustomException;
import com.abcm.dl_service.util.CommonUtils;
import com.abcm.dl_service.util.SendFailureEmail;
import com.abcm.dl_service.util.ValidatePanRequest;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Merchant_Master_Details;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DlVerifyServiceImpl implements DlVerifyService {

	private final ServiceProviderApiCall apiCall;
	private final AsyncPanRequestSaveService asyncPanRequestSaveService;
	@Value("${merchantDetails}")
	private String merchantDetails;
	private final Environment environment;
	private final ValidatePanRequest validatePanRequest;
	private final DlRequestDispatcher dispatcher;
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
	public ResponseModel processDlVerification(DlVerifyRequest request, String appId, String apiKey) {
		log.info("Start driving-license Verificaation:{processDlVerification Method}",request);
		validatePanRequest.validateDlRequest(request);
		ProductDetailsDto productDetailsDto = getProdcutDetails(request.getMerchantId());
		validatePanRequest.checkBalance(request.getMerchantId(),productDetailsDto.getMerchantName());
		validatePanRequest.validateApiCredentials(productDetailsDto, appId, apiKey);
		Map<String, Object> panProviderRequest = dispatcher.DlProviderRequestBody(productDetailsDto.getProviderName(), request);
		log.info("driving-license dyanamic provider request body{}",panProviderRequest);
		KycData kycData = asyncPanRequestSaveService.saveDlAsync(request, panProviderRequest, productDetailsDto.getProviderName(), productDetailsDto.getProductName(), productDetailsDto.getMerchantName());
		return handleDlVerificationAsync(panProviderRequest, productDetailsDto,  kycData.getTrackId(),kycData.getOrderId());
	}
	private ResponseModel handleDlVerificationAsync(Map<String, Object> panProviderRequest, ProductDetailsDto productDetailsDto, String trackId,String orderId) {
		log.info("Api Call Method Dl{} "+orderId);
		String apiResponse=apiCall.providerApiCall(panProviderRequest, productDetailsDto);
		log.info("DL Api Response Before Handle{}"+apiResponse);
		//apiCall.providerApiCall(panProviderRequest, productDetailsDto);
		     //log.info("Driving Ls Respopnse"+apiResponse);
		if (apiResponse == null || apiResponse.isBlank() || "fail:false".equalsIgnoreCase(apiResponse) || !apiResponse.trim().startsWith("{")) {
			log.error("API response is null, empty, 'fail:false', or not valid JSON: " + apiResponse);
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
		JSONObject responseObj = new JSONObject(apiResponse);
		return responseDispatcher.DlResponse(
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
		log.info("The merchant Active status is " + merchantMaster.getProductDetails());
		if (merchantMaster == null || merchantMaster.getProductDetails() == null) {
			throw new RuntimeException("Merchant or Product details missing");
		}
		return merchantMaster.getProductDetails().stream()
				.filter(product -> "DRIVING_LICENSE".equalsIgnoreCase(product.getProductName()))
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
									d.setDriving_license(merchantMaster.getDriving_license());
									d.setProductId(product.getProductId());
									d.setProductName(product.getProductName());
									d.setProviderId(provider.getProviderId());
									d.setProviderName(provider.getProviderName());
									d.setProviderAppId(provider.getProviderAppId());
									d.setProviderAppkey(provider.getProviderAppkey());
									d.setDrivingLsUrl(provider.getDrivingLicenseUrl());
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
