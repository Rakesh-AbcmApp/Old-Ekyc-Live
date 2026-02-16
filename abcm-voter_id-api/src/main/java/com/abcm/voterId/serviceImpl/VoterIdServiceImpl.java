package com.abcm.voterId.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.abcm.voterId.DTO.MerchantResponse;
import com.abcm.voterId.DTO.ProductDetailsDto;
import com.abcm.voterId.DTO.ResponseModel;
import com.abcm.voterId.DTO.VoterIdRequestModel;
import com.abcm.voterId.apiCall.ServiceProviderApiCall;
import com.abcm.voterId.dyanamicProviderResponse.ResponseDispatcher;
import com.abcm.voterId.dyanamicRequestBody.VoterIdRequestDispatcher;
import com.abcm.voterId.exception.CustomException;
import com.abcm.voterId.service.AsyncPanRequestSaveService;
import com.abcm.voterId.service.VerifyVoterIdService;
import com.abcm.voterId.util.CommonUtils;
import com.abcm.voterId.util.SendFailureEmail;
import com.abcm.voterId.util.ValidiateVoterIdRequest;
import com.abcmkyc.entity.KycData;
import com.abcmkyc.entity.Merchant_Master_Details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoterIdServiceImpl implements VerifyVoterIdService {

	private final ValidiateVoterIdRequest validiateVoterIdRequest;
	
	private final ServiceProviderApiCall apiCall;
	
	private final AsyncPanRequestSaveService asyncPanRequestSaveService;
	
	@Value("${merchantDetails}")
	private String merchantDetails;
	
	private final Environment environment;
	
	private final ValidiateVoterIdRequest voterIdRequestvalidator;
	
	private final VoterIdRequestDispatcher dispatcher;
	
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
	public ResponseModel verifyVoterId(VoterIdRequestModel request, String appId, String apiKey) {

		
		log.info("verify voter id service method{}",request);
		validiateVoterIdRequest.validateVoterIdRequest(request);
		
		ProductDetailsDto productDetailsDto = getProdcutDetails(request.getMerchantId());
		voterIdRequestvalidator.checkBalance(request.getMerchantId(),productDetailsDto.getMerchantName());
		voterIdRequestvalidator.validateApiCredentials(productDetailsDto, appId, apiKey);

		Map<String, Object> voterIdProviderRequest = dispatcher
				.VoterIdProviderRequestBody(productDetailsDto.getProviderName(), request);
		
		log.info("Requesy Body Dyanamic{}",voterIdProviderRequest);

		KycData kycData = asyncPanRequestSaveService.savePanAsync(request, voterIdProviderRequest,
				productDetailsDto.getProviderName(), productDetailsDto.getProductName(),
				productDetailsDto.getMerchantName());
		log.info("after saving the voter-id request{}",kycData.getOrderId(),kycData.getMerchantId());

		return handleVoterIdVerificationAsync(voterIdProviderRequest, productDetailsDto, kycData.getTrackId(),kycData.getOrderId());
	}

	private ResponseModel handleVoterIdVerificationAsync(Map<String, Object> panProviderRequest,
			ProductDetailsDto productDetailsDto, String trackId, String orderId) {
		log.info("Api Call handleVoterIdVerificationAsync");
		String apiResponse1 = """
								{
				    "request_id": "ddb657d5-b014-4314-8d13-22e744f523f8",
				    "task_id": "d15a2a3b-9989-46ef-9b63-e24728292dc1",
				    "group_id": "3510579a-744f-47e5-9528-fdff119b200c",
				    "success": true,
				    "response_code": "100",
				    "response_message": "Valid Authentication",
				    "metadata": {
				        "billable": "Y"
				    },
				    "result": {
				        "address": {
				            "district_code": 22,
				            "district_name": "Mumbai Suburban",
				            "district_name_vernacular": "मुंबई उपनगर",
				            "state": "Maharashtra",
				            "state_code": "S13"
				        },
				        "user_age": 42,
				        "assembly_constituency_name": "Jogeshwari East",
				        "assembly_constituency_name_vernacular": "जोगेश्वरी पूर्व",
				        "assembly_constituency_number": 158,
				        "constituency_part_name": "Harinagar Mun.Urdu School Campus- (Pandal-C)\t",
				        "constituency_part_name_vernacular": "हरीनगर म.न. पा. उर्दू शाळा आवारातील (मंडप-सी)\t",
				        "constituency_part_number": 268,
				        "constituency_section_number": 22,
				        "epic_number": "FCS2023109",
				        "user_gender": "M",
				        "parliamentary_constituency_name": "Mumbai North West",
				        "parliamentary_constituency_name_vernacular": "",
				        "parliamentary_constituency_number": "27",
				        "polling_booth": {
				            "lat_long": "19.130152-72.858008",
				            "name": "Harinagar Municipal Urdu School No.1",
				            "name_vernacular": "",
				            "number": 268
				        },
				        "relative_name_english": "Narayan",
				        "relative_name_vernacular": "नारायण",
				        "relative_relation": "FTHR",
				        "serial_number_applicable_part": 703,
				        "status": "N",
				        "user_name_english": "Rakesh Bhardwaj",
				        "user_name_vernacular": "भुपेश नारायण कापडिया",
				        "voter_last_updated_date": "2025-04-24T20:16:21.176+00:00"
				    },
				    "request_timestamp": "2025-07-16T05:19:48.371Z",
				    "response_timestamp": "2025-07-16T05:19:48.441Z"
				}

								""";

		String apiResponse2 = """
								{
				    "verification_id": "1239",
				    "reference_id": 127175,
				    "epic_number": "UAI4574761",
				    "status": "VALID",
				    "name": "HARSHIT PRAJAPATI",
				    "name_in_regional_lang": "हर्षित प्रजापति",
				    "age": "23",
				    "relation_type": "FTHR",
				    "relation_name": "RAJ PRAJAPATI",
				    "relation_name_in_regional_lang": "राज प्रजापति",
				    "father_name": "RAJ PRAJAPATI",
				    "dob": "2000-12-07",
				    "gender": "Male",
				    "state": "Madhya Pradesh",
				    "assembly_constituency_number": "211",
				    "assembly_constituency": "SANER",
				    "parliamentary_constituency_number": "25",
				    "parliamentary_constituency": "INDORE",
				    "part_number": "27",
				    "part_name": "SIRWAR",
				    "serial_number": "713",
				    "polling_station": "GOVT JUNIOR COLLEGE SIRWAR",
				    "address": "GOVT JUNIOR COLLEGE SIRWAR",
				    "photo": "https://cf-sbox-payoutbankvalidationsvc-biometric-image.s3.ap-south-1.amazonaws.com/identityVerification/VOTER_ID/127175_photo.png.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIASWG7WQ7N53D3XLQQ%2F20250717%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Date=20250717T074736Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=9858dbfca8531788838ce2bead96f7a2e332825142e4a4fd8715033e7f7d5658",
				    "split_address": {
				        "district": [
				            "INDORE"
				        ],
				        "state": [
				            [
				                "Madhya Pradesh"
				            ]
				        ],
				        "city": [
				            "SANER"
				        ],
				        "pincode": "560034",
				        "country": [
				            "IN",
				            "IND",
				            "INDIA"
				        ],
				        "address_line": "GOVT JUNIOR COLLEGE SIRWAR"
				    }
				}
								""";

		String apiResponse3 = """
								{
				    "request_id": "882fb952-9eec-4c69-b051-263647bb5fee",
				    "task_id": "d15a2a3b-9989-46ef-9b63-e24728292dc0",
				    "group_id": "809ee25f-6919-42b2-b6e6-5a8864fb5dce",
				    "success": false,
				    "response_code": "106",
				    "response_message": "Invalid ID Number or Combination of Inputs",
				    "metadata": {
				        "billable": "N",
				        "reason_message": "Invalid format for Voter EPIC number"
				    },q
				    "result": null,
				    "request_timestamp": "2025-07-18T05:52:36.216Z",
				    "response_timestamp": "2025-07-18T05:52:36.218Z"
				}
								""";

		String apiResponse = apiCall.providerApiCall(panProviderRequest, productDetailsDto);
		log.info("voteri API response : {}",apiResponse);
		if (apiResponse == null || apiResponse.isBlank() || "fail:false".equalsIgnoreCase(apiResponse)
				|| !apiResponse.trim().startsWith("{")) {
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
		return responseDispatcher.getVoterIdResponse(productDetailsDto.getProviderName(), responseObj, trackId,
				productDetailsDto.getMerchantId(), productDetailsDto.getProductRate(),orderId);
	}
	
	
	public ProductDetailsDto getProdcutDetails(String mid) {
		Merchant_Master_Details merchantMaster = getMerchantByMid(mid); // no caching
		return extractOkycProductDetails(merchantMaster);
	}
	public Merchant_Master_Details getMerchantByMid(String mid) {
		log.info("Fetching Merchant Details for MID: {}", mid);
		WebClient webClient = WebClient.builder().baseUrl(merchantDetails).build();
		MerchantResponse response = webClient.get().uri(uriBuilder -> uriBuilder
				.path("/api/merchant-kyc-routing/merchant-details").queryParam("merchantId", mid).build()).retrieve()
				.bodyToMono(MerchantResponse.class).block();
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
		log.info("The merchant Active status is " + merchantMaster.isActive());
		if (merchantMaster == null || merchantMaster.getProductDetails() == null) {
			throw new RuntimeException("Merchant or Product details missing");
		}

		log.info("merchant master details : {} ", merchantMaster);
		return merchantMaster.getProductDetails().stream()
				.filter(product -> "VOTER-ID".equalsIgnoreCase(product.getProductName()))
				.flatMap(product -> product.getProviders().stream()
						.flatMap(provider -> provider.getMerchantCharges().stream().map(charges -> {
							ProductDetailsDto d = new ProductDetailsDto();
							d.setId(merchantMaster.getId());
							d.setMerchantId(merchantMaster.getMerchantId());
							d.setMerchantName(merchantMaster.getMerchantName());
							d.setAppId(merchantMaster.getAppId());
							d.setApiKey(merchantMaster.getApiKey());
							d.setActive(merchantMaster.isActive());
							d.setVoter_id(merchantMaster.getVoter_id());
							d.setProductId(product.getProductId());
							d.setProductName(product.getProductName());
							d.setProviderId(provider.getProviderId());
							d.setProviderName(provider.getProviderName());
							d.setProviderAppId(provider.getProviderAppId());
							d.setProviderAppkey(provider.getProviderAppkey());
							d.setVoterIdUrl(provider.getVoterIdUrl());
							d.setRouteId(charges.getRouteId());
							d.setProductRate(charges.getProductRate());
							return d;
						})))
				.findFirst().orElseThrow(() -> new CustomException(environment.getProperty("custom.messages.Not-Found"),
						Integer.parseInt(environment.getProperty("custom.codes.Not-Found"))));
	}

}
