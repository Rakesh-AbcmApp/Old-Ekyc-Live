package com.abcm.addhar_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.abcm.addhar_service.Email.CommonUtils;
import com.abcm.addhar_service.Email.SendFailureEmail;
import com.abcm.addhar_service.apicall.ServiceProviderApiCall;
import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.createRequestbody.AadhaarRequestDispatcher;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.ProductDetailsDto;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.dto.VerifyOtpRequest;
import com.abcm.addhar_service.dyanamicReponse.ResponseDispatcher;
import com.abcm.addhar_service.entity.MerchantResponse;
import com.abcm.addhar_service.exception.CustomException;
import com.abcm.addhar_service.repository.AadharRepository;
import com.abcm.addhar_service.util.RequestValidator;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Merchant_Master_Details;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AadharOtpServiceImpl implements AadhaarOtpService {
	private final ServiceProviderApiCall apiCall;

	private final AsyncAaddharRequestSaveService aaddharRequestSaveService;

	private final Environment environment;

	private final RequestValidator requestValidator;

	private final AadhaarRequestDispatcher aadhaarRequestDispatcher;

	private final ResponseDispatcher responseDispatcher;

	@Value("${email.serviceDown.subject}")
	private String serviceDownSubject;

	@Value("${email.serviceDown.template.path}")
	private String serviceDownTemplatePath;

	@Value("${Env}")
	private String env;

	@Autowired
	private SendFailureEmail sendFailureEmail;

	@Value("${email.send.to}")
	private String to;

	private final AadharRepository aadharRepository;

	@Override
	public ResponseModel aadharOtpRequest(AadhaarOtpRequest request, String appId, String apiKey) {
		try {
			
			log.info("aadharOtpRequest{}:"+request.toString());
			requestValidator.validateAadhaarOtpRequest(request);
			ProductDetailsDto merchant = getOkycDetailsByMid(request.getMerchant_id());
			log.info("The Merchnat details info{}");
			requestValidator.validateMerchantBalance(merchant.getMerchantId(), merchant.getMerchantName());
			requestValidator.validateApiCredentialsAndStatus(merchant, appId, apiKey);
			// dynamic request
			Map<String, Object> requestBody = aadhaarRequestDispatcher.getSendOtpRequestBody(merchant.getProviderName(),
					request);
			log.info("Provider Request body" + requestBody);
			KycData data=null;
			try
			{
				data = aaddharRequestSaveService.saveAadharAsync(request, merchant.getProviderName(),
						merchant.getProductName(), requestBody, merchant.getMerchantName());
			}catch (Exception e) {
				log.error("error while data saved",e);
			}
			return processOtpRequest(requestBody, request, merchant, data.getTrackId(), data.getMerchantId(),
					data.getOrderId());
			
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomException(environment.getProperty("custom.messages.internal-server"),
					Integer.parseInt(environment.getProperty("custom.internal-server")));
		}
	}
	
	
	
	private ResponseModel processOtpRequest(Map<String, Object> requestBody, AadhaarOtpRequest request,
			ProductDetailsDto merchant_Master, String TrackId, String merchantId,String orderId)
			throws JsonProcessingException {
		try {
			log.info("Otp Send Api Call Method Inside{}");
			String apiResponse1="{\r\n"
					+ "  \"code\": 400,\r\n"
					+ "  \"message\": \"failed\",\r\n"
					+ "  \"status\": {\r\n"
					+ "    \"created_at\": \"Wed, 13 Aug 2025 10:33:43 GMT\",\r\n"
					+ "    \"ref_id\": \"V27492733236191488530\",\r\n"
					+ "    \"serviceName\": \"aadhaar_offline\",\r\n"
					+ "    \"statusCode\": 2201,\r\n"
					+ "    \"statusMessage\": \"Offline Aadhaar Initiate Failed :Aadhaar number not linked to mobile\"\r\n"
					+ "  }\r\n"
					+ "}";
			
			String apiResponse = apiCall.providerApiCall(requestBody, merchant_Master.getProviderAppkey(),merchant_Master.getProviderAppId(), merchant_Master.getAadhaarOtpSendUrl(),merchant_Master.getProviderName());
			log.info("Provider Api Response after call" + apiResponse);
			if ("fail:false".equalsIgnoreCase(apiResponse) || apiResponse == null || apiResponse.isBlank()) {
				log.error("API response is null or empty");
				CompletableFuture.runAsync(() -> {
					try {
						log.info("Email Template Path: {}", serviceDownTemplatePath);
						String mailstring1 = CommonUtils.readUsingFileInputStream(serviceDownTemplatePath);
						mailstring1 = mailstring1.replace("{{providerName}}", merchant_Master.getProviderName());
						mailstring1 = mailstring1.replace("{{product}}", merchant_Master.getProductName());
						mailstring1 = mailstring1.replace("{{Environment}}", env);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
						// Get current date-time
						String timestamp = LocalDateTime.now().format(formatter);
						mailstring1 = mailstring1.replace("{{Timestamp}}", timestamp);
						sendFailureEmail.sendAadhaarFailureEmail(mailstring1, "", serviceDownSubject, to);
	 
					} catch (Exception e) {
						e.printStackTrace(); // Consider logging instead
					}
				});
				throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
						Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
			}
			JSONObject responseObj = new JSONObject(apiResponse);
			ResponseModel responseModel = responseDispatcher.getResponse(merchant_Master.getProviderName(), responseObj,
					TrackId, merchantId,merchant_Master.getProductRate(),orderId);
			return responseModel;
		}catch (Exception e) {
			log.error("processOtpRequest exception");
			throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
					Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
			
		}
		
	}

	
	
	
	
	

	@Override
	public ResponseModel processAadhaarOtpVerification(VerifyOtpRequest verifyOtpRequest, String appId, String apiKey) {
		log.info("OTP Verify Hit To Servvice Method"); // Step 1: Validate request and merchant
		requestValidator.validateVerifyOtpRequest(verifyOtpRequest);
		ProductDetailsDto merchant = getOkycDetailsByMid(verifyOtpRequest.getMerchant_id());
		requestValidator.validateMerchantBalance(merchant.getMerchantId(), merchant.getMerchantName());
		requestValidator.validateApiCredentialsAndStatus(merchant, appId, apiKey);
			if (aadharRepository.existsByMerchantIdAndRequestIdAndOtpVerifyTrue(verifyOtpRequest.getMerchant_id(),
					verifyOtpRequest.getRequest_id())) {
				throw new CustomException(
						String.format("Aadhaar already verified for requestId please try fresh Request"), 409);
			}
			
			
		         log.info("get  before data  Request ID" + verifyOtpRequest.getRequest_id());
		         KycData data = aadharRepository
				.findByMerchantAndRequestId(verifyOtpRequest.getMerchant_id(), verifyOtpRequest.getRequest_id())
				.orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.request-id-not-found"),
						Integer.parseInt(environment.getProperty("custom.codes.request-id-not-found"))));
			log.info("get  after data  Request ID" + data.getRequestId());
			
			Map<String, Object> requestBody = aadhaarRequestDispatcher
					.getVerifyOtpRequestBody(merchant.getProviderName(), verifyOtpRequest, data.getRequestId());
			
			log.info("Verify otp provider  request body" + requestBody);
			
		return serviceProviderOtpVerifyApiCall(requestBody, verifyOtpRequest, merchant, data);

	}
	private ResponseModel serviceProviderOtpVerifyApiCall(Map<String, Object> requestBody,
			VerifyOtpRequest verifyOtpRequest, ProductDetailsDto merchant, KycData data) {
		try
		{
			String apiResponse1 = "{\r\n"
					+ "    \"code\": 200,\r\n"
					+ "    \"data\": {\r\n"
					+ "        \"aadhar_ref\": \"706120250818174435656\",\r\n"
					+ "        \"address\": \",PAINDGAON,PHULAMBRI,Aurangabad,Maharashtra-431134\",\r\n"
					+ "        \"care_of\": \"S/O Kacharu Dakle\",\r\n"
					+ "        \"date_of_birth\": \"08-08-1995\",\r\n"
					+ "        \"email\": \"\",\r\n"
					+ "        \"freshRequest\": \"true\",\r\n"
					+ "        \"gender\": \"M\",\r\n"
					+ "        \"masked_aadhar\": \"**** ****  7061\",\r\n"
					+ "        \"mobile_hash\": \"00c2f7722c29683a8b43a54461c7d10182c15284bcef5be7514cf9cc52dce79d\",\r\n"
					+ "        \"name\": \"Krishna Kacharu Dakle\",\r\n"
					+ "        \"photo_link\": \"https://storage.googleapis.com/offline-aadhar/offline_aadhaar/V10305161046148572161/986494d8c8a18539389139a73ec79112e06f20c861e5219389c6a13928e87ffd.jpg?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=pichain-all-access%40bam-cloud.iam.gserviceaccount.com%2F20250818%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20250818T121439Z&X-Goog-Expires=3600&X-Goog-SignedHeaders=host&X-Goog-Signature=42496e2c39763f56936d299c20d10642153548f71928091aa45b75dfb9c3ed673129972f8d0bab1ad98c4906b3a7cd631d2917c13624ca4a9364393e96d37affe6772bb6be392ab568023f98fb98b93c2e3bdfb94d9cee0b2ba02ccdad58c9d717bba8704f5ed31ab3d9131e69d1151c14e7eab75721d7015cc5d1514cfdea0c99dde09159d807a08ffdc6655be7b5eff7a04fe1f790a1c68efbbab134294b4a28accea5160825696ddc7f977cef2a3f841b281a39022e3de99be10d2a60f27f1bb3aefef6dc1d5972d58112644b6cfb8987f7b4669db8a18b0c5177bc880558bb75a2e2b9bc0d2ac8c2f7e21ccdd957002d65303251939e5d4ffadec258d152\",\r\n"
					+ "        \"share_code\": \"7213\",\r\n"
					+ "        \"split_address\": {\r\n"
					+ "            \"country\": \"India\",\r\n"
					+ "            \"dist\": \"Aurangabad\",\r\n"
					+ "            \"house\": \"\",\r\n"
					+ "            \"landmark\": \"\",\r\n"
					+ "            \"loc\": \"PAINDGAON,PHULAMBRI\",\r\n"
					+ "            \"pincode\": \"431134\",\r\n"
					+ "            \"po\": \"Badod Bazar\",\r\n"
					+ "            \"state\": \"Maharashtra\",\r\n"
					+ "            \"street\": \"\",\r\n"
					+ "            \"subdist\": \"\",\r\n"
					+ "            \"vtc\": \"PAINDGAON\"\r\n"
					+ "        },\r\n"
					+ "        \"xml_file_path\": \"https://storage.googleapis.com/offline-aadhar/offline_aadhaar/V10305161046148572161/offlineaadhaar20250818054435662.xml?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=pichain-all-access%40bam-cloud.iam.gserviceaccount.com%2F20250818%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20250818T121439Z&X-Goog-Expires=3600&X-Goog-SignedHeaders=host&X-Goog-Signature=c610212710ab3a5758c297877565ed30140e8a1792fb594f56a70632034389eef45bfe50e9138dab99580c573b5b975e9e5f029ca0300276411c29c197ccca76a9d3db7484e044d3d277c79bf44e333155f6c043486227f51df16f3d5005011fc7e9533b55134b782fc510c03a8974e67fb06d885b7c7aafeba2ee79121a7041188722968ce77764b99f6a006ac0878985b3d3402ce413e4fd2e93bfc487bb649c33267c3139f163280e837a5941ae4ee56b810b91dc01dfdd4b3581d6d357c0b823e99a434d2fced300f57ba94546a2773c8a3849e2e1c7a839ad104f9f98fc8b0a53628d14c097b900faf1f5f51c5d426eea9bcaaad7005d5a1582a254d5a9\",\r\n"
					+ "        \"year_of_birth\": \"1995\",\r\n"
					+ "        \"zip_file_path\": \"https://storage.googleapis.com/offline-aadhar/offline_aadhaar/V10305161046148572161/offlineaadhaar20250818054435662.zip?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=pichain-all-access%40bam-cloud.iam.gserviceaccount.com%2F20250818%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20250818T121439Z&X-Goog-Expires=3600&X-Goog-SignedHeaders=host&X-Goog-Signature=67c1fab2c8c36bb57cc9fe630e016e2f9e3f2c3339ce1401c1a84928655edaaab9ad33f8aeac0dfe93eb7d3f993f902c9b9d1f238baf6409345a90ef68a57192d5cb1995b467818070f54288b00136a13354ffba8b64e0263141c95845077220a6091401baa50b70d56df6a74e49198dc4a50e24a72d83adf86e81bf78ff77fb499b26a0da5c2cee1f88375547ce7ecb5d5e0736352c264b20fe47a6e07a4c9bdb5553a4f6585eaca0d29f7e825f7f6c92caa0832db2da636ff86835213c83a06a5bb46753faa6e81fd299560382f40c51ff23db5e165e0742cb3b527dc4133e478613b075130ded5617934552e2b2bff0db7ad737fee35afd982f92c7c6132a\"\r\n"
					+ "    },\r\n"
					+ "    \"message\": \"success\",\r\n"
					+ "    \"status\": {\r\n"
					+ "        \"created_at\": \"Mon, 18 Aug 2025 12:14:39 GMT\",\r\n"
					+ "        \"ref_id\": \"V21313058468449505288\",\r\n"
					+ "        \"serviceName\": \"aadhaar_offline\",\r\n"
					+ "        \"statusCode\": 2202,\r\n"
					+ "        \"statusMessage\": \"Offline Aadhaar is Succeed.\"\r\n"
					+ "    }\r\n"
					+ "}";
			String  apiResponse= apiCall.providerApiCall(requestBody, merchant.getProviderAppkey(),merchant.getProviderAppId(),merchant.getAadhaarOtpVerifyUrl(),merchant.getProviderName());
			if ("fail:false".equalsIgnoreCase(apiResponse) || apiResponse == null || apiResponse.isBlank()) {
				CompletableFuture.runAsync(() -> {
					try {
						log.info("Email Template Path: {}", serviceDownTemplatePath);
						String mailstring1 = CommonUtils.readUsingFileInputStream(serviceDownTemplatePath);
						mailstring1 = mailstring1.replace("{{providerName}}", merchant.getProviderName());
						mailstring1 = mailstring1.replace("{{product}}", merchant.getProductName());
						mailstring1 = mailstring1.replace("{{Environment}}", env);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
						// Get current date-time
						String timestamp = LocalDateTime.now().format(formatter);
						mailstring1 = mailstring1.replace("{{Timestamp}}", timestamp);
						sendFailureEmail.sendAadhaarFailureEmail(mailstring1, "", serviceDownSubject, to);
					} catch (Exception e) {
						e.printStackTrace(); // Consider logging instead
					}
				});
				log.error("API response is null or empty");
				throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
						Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
			}

			JSONObject responseObj = new JSONObject(apiResponse);
			ResponseModel responseModel = responseDispatcher.verifyOTP(merchant.getProviderName(), responseObj,
					merchant.getMerchantId(), merchant.getProductRate(), data);
			log.info("Set Dyanamic Response When Done");
			return responseModel;
	
		}catch (Exception e) {
			log.error("serviceProviderOtpVerifyApiCall");
			throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
					Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
		}
		
		
	}
	
	
	

	public Merchant_Master_Details getMerchantByMid(String mid) {
		
		log.info("Fetching Merchant Details for MID: {}", mid, environment.getProperty("merchantDetails"));
		
		WebClient webClient = WebClient.builder().baseUrl(environment.getProperty("merchantDetails")).build();
		MerchantResponse response;
		
		try {
			response = webClient.get()
					.uri(uriBuilder -> uriBuilder.path("/api/merchant-kyc-routing/merchant-details")
							.queryParam("merchantId", mid).build())
					.retrieve()
					.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> {
						log.error("API returned error status for MID: {}", mid);
						return Mono.error(new CustomException(environment.getProperty("custom.messages.Not-Found"),
								Integer.parseInt(environment.getProperty("custom.codes.Not-Found"))));
					}).bodyToMono(MerchantResponse.class).block();
		} catch (Exception e) {
			log.info("Exception to get info");
			throw new CustomException(environment.getProperty("custom.messages.Not-Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Not-Found")));
		}
		if (response == null || response.getData() == null) {
			log.error("No merchant data found for MID: {}", mid);
			throw new CustomException(environment.getProperty("custom.messages.Not-Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Not-Found")));
		}
		Merchant_Master_Details details = response.getData();
		if (details.getProductDetails() == null || details.getProductDetails().isEmpty()) {
			log.error("No product details found for merchant MID: {}", mid);
			throw new CustomException(environment.getProperty("custom.messages.product-not-found"),
					Integer.parseInt(environment.getProperty("custom.codes.product-not-found")));
		}
		log.info("Successfully fetched Merchant Details for MID: {}", mid);
		return details;
	}

	
	
	
	public ProductDetailsDto extractOkycProductDetails(Merchant_Master_Details merchantMaster) {
		log.info("The merchnat Active status is" + merchantMaster.isActive());
		if (merchantMaster == null || merchantMaster.getProductDetails() == null) {
			throw new RuntimeException("Merchant or Product details missing");
		}
		ProductDetailsDto dto = merchantMaster.getProductDetails().stream()
				.filter(product -> "OKYC".equalsIgnoreCase(product.getProductName()))
				.flatMap(product -> product.getProviders().stream()
						.flatMap(provider -> provider.getMerchantCharges().stream().map(charges -> {
							ProductDetailsDto d = new ProductDetailsDto();
							d.setId(merchantMaster.getId());
							d.setMerchantId(merchantMaster.getMerchantId());
							d.setMerchantName(merchantMaster.getMerchantName());
							d.setAppId(merchantMaster.getAppId());
							d.setApiKey(merchantMaster.getApiKey());
							d.setActive(merchantMaster.isActive());
							d.setOKYC(merchantMaster.getOKYC());
							d.setProductId(product.getProductId());
							d.setProductName(product.getProductName());
							d.setProviderId(provider.getProviderId());
							d.setProviderName(provider.getProviderName());
							d.setProviderAppId(provider.getProviderAppId());
							d.setProviderAppkey(provider.getProviderAppkey());
							d.setAadhaarOtpSendUrl(provider.getAadhaarOtpSendUrl());
							d.setAadhaarOtpVerifyUrl(provider.getAadhaarOtpVerifyUrl());
							d.setRouteId(charges.getRouteId());
							d.setProductRate(charges.getProductRate());
							return d;
						})))
				.findFirst().orElseThrow(() -> new CustomException(

						environment.getProperty("custom.messages.Not-Found"),
						Integer.parseInt(environment.getProperty("custom.codes.Not-Found"))));
		return dto;
	}

	public ProductDetailsDto getOkycDetailsByMid(String mid) {
		log.info("Merchnat Details Retrive  Method ");
		Merchant_Master_Details merchantMaster = getMerchantByMid(mid); // Step 1 ka result
		return extractOkycProductDetails(merchantMaster); // Step 2 ka use
	}

	@Override
	public ResponseModel initiateDigilocker(ZoopInitRequest request, String appId, String apiKey) {
		try {
			requestValidator.validateAadhaardigiRequest(request);
			ProductDetailsDto merchant = getOkycDetailsByMid(request.getMerchant_id());
			log.info("The Merchnat details info{} " + merchant);
			requestValidator.validateMerchantBalance(merchant.getMerchantId(), merchant.getMerchantName());
			log.info("after bal valii");
			requestValidator.validateApiCredentialsAndStatus(merchant, appId, apiKey);
			// dynamic request
			log.info("after val Creds");
			Map<String, Object> requestBody = aadhaarRequestDispatcher.getDigiRequestBody(merchant.getProviderName(),
					request);
			// Map<String, Object> requestBody =
			// aadhaarRequestDispatcher.getSendOtpRequestBody(merchant.getProviderName(),
			// request);
			log.info("Provider Request body" + requestBody);
			// save data to kyc_data
			KycData data = aaddharRequestSaveService.saveAadharDigiAsync(request, merchant.getProviderName(),
					merchant.getProductName(), requestBody, merchant.getMerchantName(), merchant.getProductRate());
			// api call to processor
			return processDigiRequest(requestBody, request, merchant, data.getTrackId(), data.getMerchantId(),
					data.getOrderId());
		} catch (CustomException e) {
			log.error("Unhandled exception in initiateDigilocker");
			throw e;
		} catch (Exception e) {
			log.error(" Unhandled exception in initiateDigilocker");
			throw new CustomException(
					environment.getProperty("custom.messages.Not-Found", "Merchant Details Not Found"),
					Integer.parseInt(environment.getProperty("custom.codes.Not-Found", "404")) // ✅ Fallback
			);

		}
	}

	private ResponseModel processDigiRequest(Map<String, Object> requestBody, ZoopInitRequest request,
			ProductDetailsDto merchant_Master, String TrackId, String merchantId, String orderId)
			throws JsonProcessingException {
		log.info("processDigiRequest{}");
		String apiResponse = apiCall.providerApiCall(requestBody, merchant_Master.getProviderAppkey(),
				merchant_Master.getProviderAppId(), merchant_Master.getAadhaarOtpSendUrl(),
				merchant_Master.getProviderName());
		if ("fail:false".equalsIgnoreCase(apiResponse) || apiResponse == null || apiResponse.isBlank()) {
			log.error("API response is null or empty");

			CompletableFuture.runAsync(() -> {
				try {
					log.info("Email Template Path: {}", serviceDownTemplatePath);
					String mailstring1 = CommonUtils.readUsingFileInputStream(serviceDownTemplatePath);
					mailstring1 = mailstring1.replace("{{Service_Name}}", merchant_Master.getProductName());
					mailstring1 = mailstring1.replace("{{providerName}}", merchant_Master.getProviderName());
					mailstring1 = mailstring1.replace("{{Environment}}", env);
					mailstring1 = mailstring1.replace("{{Reason}}", "The Provider service "
							+ merchant_Master.getProviderName() + " is currently unavailable. Please try again later");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
					// Get current date-time
					String timestamp = LocalDateTime.now().format(formatter);
					mailstring1 = mailstring1.replace("{{Timestamp}}", timestamp);
					sendFailureEmail.sendAadhaarFailureEmail(mailstring1, "", serviceDownSubject, to);

				} catch (Exception e) {
					e.printStackTrace(); // Consider logging instead
				}
			});

			throw new CustomException(environment.getProperty("custom.messages.provider-invalid-res"),
					Integer.parseInt(environment.getProperty("custom.codes.provider-invalid-res")));
		}

		JSONObject responseObj = new JSONObject(apiResponse);
		ResponseModel responseModel = responseDispatcher.digiVerify(merchant_Master.getProviderName(), responseObj,
				TrackId, merchantId, orderId);
		return responseModel;
	}

	
	
	
	
}
