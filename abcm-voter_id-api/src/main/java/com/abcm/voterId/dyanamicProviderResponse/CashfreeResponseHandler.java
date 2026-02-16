package com.abcm.voterId.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.abcm.voterId.DTO.ResponseModel;
import com.abcm.voterId.DTO.VoterIdResponseToMerchant;
import com.abcm.voterId.util.UpdateBalance;
import com.abcm.voterId.util.VoterIdResponseUpdate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CashfreeResponseHandler implements ProviderResponseHandler<ResponseModel> {

	private final Environment environment;
	private final VoterIdResponseUpdate voterIdResponseUpdate;
	private final UpdateBalance updateBalance;
	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneOffset.UTC);
	private static final String STATUS_VALID = "VALID";
	private static final String TYPE_VALIDATION_ERROR = "validation_error";
	private static final String TYPE_RATE_LIMITE_ERROR = "rate_limit_error";
	private static final String INTERNAL_ERROR = "internal_error";
	private final Map<String, VoterIdResponseToMerchant> responseCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() { // Initialize predefined responses
		responseCache.put(STATUS_VALID, buildResponse("voterId-validation-msg", true));
		responseCache.put("voterId_length_short", buildResponse("voterId-format-validation", false));
		responseCache.put("invalid_verification_id", buildResponse("verification-id-msg", false));
		responseCache.put("insufficient_balance", buildResponse("porvider-blc-low", false));
		responseCache.put("too_many_requests_per_operation", buildResponse("porvider-blc-low", false));
		responseCache.put("internal_error", buildResponse("voterId-validation-failed-msg", false));
		responseCache.put("epic_number_value_invalid", buildResponse("voterId-format-validation", false));
		responseCache.put("default", buildResponse("voterId-validation-failed-msg", false));

	}

	private VoterIdResponseToMerchant buildResponse(String keyPrefix, boolean success) {
		return VoterIdResponseToMerchant.builder().response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix)).success(success).build();
	}

	private VoterIdResponseToMerchant getCachedResponse(String status, String type, String code) {

		log.info("Inside getCachedResponse method  status : {} type : {} code : {} ", status, type, code);
		if (STATUS_VALID.equalsIgnoreCase(status)) {
			return responseCache.getOrDefault(STATUS_VALID, responseCache.get("default"));
		}

		if (TYPE_VALIDATION_ERROR.equalsIgnoreCase(type)) {

			if ("voterId_length_short".equalsIgnoreCase(code)) {
				log.error("voterId format issue detected");
				return responseCache.getOrDefault("voterId_length_short", responseCache.get("default"));
			}
			if ("invalid_verification_id".equalsIgnoreCase(code)) {
				log.error("Invalid verification ID issue");
				return responseCache.getOrDefault("invalid_verification_id", responseCache.get("default"));
			}

			if ("insufficient_balance".equalsIgnoreCase(code)) {
				log.error("Invalid verification ID issue");
				return responseCache.getOrDefault("insufficient_balance", responseCache.get("default"));
			}
			if ("epic_number_value_invalid".equalsIgnoreCase(code)) {
				log.error("epic_number_value_invalid");
				return responseCache.getOrDefault("epic_number_value_invalid", responseCache.get("default"));
			}

		}
		if (TYPE_RATE_LIMITE_ERROR.equalsIgnoreCase("too_many_requests_per_operation")) {
			log.error("Invalid verification ID issue");
			return responseCache.getOrDefault("too_many_requests_per_operation", responseCache.get("default"));
		}

		if (INTERNAL_ERROR.equalsIgnoreCase("internal_error")) {
			log.error("Invalid verification ID issue");
			return responseCache.getOrDefault("internal_error", responseCache.get("default"));
		}

		return responseCache.get("default");
	}

	@Override
	public ResponseModel voterIdVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId,
			Long productRate,String orderId) {
		log.info("cashfree response handler",merchantId,orderId);
		String timestamp = ISO_FORMATTER.format(Instant.now());
		String refId = responseObj.optString("reference_id", null);
		String status = responseObj.optString("status", "").toUpperCase();
		String type = responseObj.optString("type", "");
		String code = responseObj.optString("code", "");
		String Message = responseObj.optString("message", "");
		boolean isValid = STATUS_VALID.equals(status);
		String ResonMessage;
		if(!isValid)
		{
			ResonMessage=Message;
		}else
		{
			ResonMessage=status;
		}
		

		log.info("voterId verification processing for trackId: {}, status: {}, type: {}, code: {}", trackId, status,
				type, code);

		VoterIdResponseToMerchant baseResponse = getCachedResponse(status, type, code);

		

		// && "voterId verified successfully".equalsIgnoreCase(message)

		log.info("isValid : {} ", isValid);
		String billable = isValid ? "Y" : "N";

		log.info("Billable : {} ", billable);

		String responseStatus = isValid ? "success" : "failed";
		int responseCode = isValid ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();

		if (isValid) {
			log.info("deduct wallet balance inside",productRate);
			updateBalance.updateWalletBalance(merchantId, productRate);
		}

		log.info("Cf response code : {} ", responseCode);

		VoterIdResponseToMerchant response;
         String CustomerName="";
		if (isValid) { // Extract user address if present
          CustomerName=responseObj.optString("name");
			VoterIdResponseToMerchant.VoterIdResponseToMerchantBuilder builder = baseResponse.toBuilder()
					.referenceId(refId).track_id(trackId).merchant_id(merchantId).responseTimestamp(timestamp);
			log.info("response Object : {} ", responseObj);

			builder.epicNumber(responseObj.optString("epic_number")).status(responseObj.optString("status"))
					.name(responseObj.optString("name")).gender(responseObj.optString("gender"))
					.age(responseObj.optInt("age")).relationType(responseObj.optString("relation_type"))
					.relationName(responseObj.optString("relation_name"))
					.parliamentaryConstituencyNumber(responseObj.optInt("parliamentary_constituency_number"))
					.parliamentaryConstituency(responseObj.optString("parliamentary_constituency"))
					.assemblyConstituencyNumber(responseObj.optInt("assembly_constituency_number"))
					.assemblyConstituency(responseObj.optString("assembly_constituency"))
					.partNumber(responseObj.optString("part_number")).partName(responseObj.optString("part_name"))
					.serialNumber(responseObj.optString("serial_number"))
					.pollingStationName(responseObj.optString("polling_station")).requestTimestamp(timestamp)
					.responseTimestamp(timestamp).billable(billable);
					//.order_id(orderId);

			response = builder.build();

		} else { // Minimal keys only for failure - no personal info keys
			VoterIdResponseToMerchant.VoterIdResponseToMerchantBuilder builder = baseResponse.toBuilder()
					.referenceId(refId).track_id(trackId).merchant_id(merchantId).responseTimestamp(timestamp)
					.response_code(baseResponse.getResponse_code()).response_message(baseResponse.getResponse_message())
					.billable(billable).success(baseResponse.getSuccess());
					//.order_id(orderId);
			        
			response = builder.build();
			response.setAge(response.getAge() != null && response.getAge() == 0 ? null : response.getAge());
			response.setParliamentaryConstituencyNumber(response.getParliamentaryConstituencyNumber() != null
					&& response.getParliamentaryConstituencyNumber() == 0 ? null
							: response.getParliamentaryConstituencyNumber());
			response.setAssemblyConstituencyNumber(
					response.getAssemblyConstituencyNumber() != null && response.getAssemblyConstituencyNumber() == 0
							? null
							: response.getAssemblyConstituencyNumber());

		}

		log.info("voterId  cashfree  response to merchant: {}", response);
		voterIdResponseUpdate.updateVoterIdResponse(trackId, response, responseObj, isValid, refId, productRate,CustomerName,ResonMessage);
		log.info("after update voter id response to db{}");
		return new ResponseModel(responseStatus, responseCode, response);
	}

}
