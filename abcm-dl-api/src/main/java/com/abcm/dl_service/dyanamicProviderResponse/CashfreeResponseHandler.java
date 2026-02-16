package com.abcm.dl_service.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.abcm.dl_service.dto.DlResponseToMerchant;
import com.abcm.dl_service.dto.ResponseModel;
import com.abcm.dl_service.util.DlResponseUpdate;
import com.abcm.dl_service.util.ToSafeStringMapList;
import com.abcm.dl_service.util.UpdateBalance;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CashfreeResponseHandler implements ProviderResponseHandler<ResponseModel> {

	@Autowired
	private Environment environment;

	@Autowired
	private DlResponseUpdate dlResponseUpdate;

	@Autowired
	private UpdateBalance updateBalance;

	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneOffset.UTC);

	private static final String STATUS_VALID = "VALID";
	private static final String TYPE_VALIDATION_ERROR = "validation_error";
	private static final String TYPE_RATE_LIMITE_ERROR = "rate_limit_error";
	private static final String internal_error = "internal_error";

	private final Map<String, DlResponseToMerchant> responseCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		// Initialize predefined responses
		responseCache.put(STATUS_VALID, buildResponse("dl-validation-msg", true));
		responseCache.put("dob_value_invalid", buildResponse("dl-dob-format-validation", false));
		responseCache.put("invalid_verification_id", buildResponse("verification-id-msg", false));
		responseCache.put("insufficient_balance", buildResponse("porvider-blc-low", false));
		responseCache.put("too_many_requests_per_operation", buildResponse("porvider-blc-low", false));
		responseCache.put("internal_error", buildResponse("dl-validation-failed-msg", false));
		responseCache.put("default", buildResponse("dl-validation-failed-msg", false));
	}
	private DlResponseToMerchant buildResponse(String keyPrefix, boolean success) {
		return DlResponseToMerchant.builder().response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix)).success(success).build();
	}

	private DlResponseToMerchant getCachedResponse(String status, String type, String code) {
		if (STATUS_VALID.equalsIgnoreCase(status)) {
			return responseCache.getOrDefault(STATUS_VALID, responseCache.get("default"));
		}

		if (TYPE_VALIDATION_ERROR.equalsIgnoreCase(type)) {
			if ("dob_value_invalid".equalsIgnoreCase(code)) {
				log.error("dob_value_invalid");
				return responseCache.getOrDefault("dob_value_invalid", responseCache.get("default"));
			}
			if ("invalid_verification_id".equalsIgnoreCase(code)) {
				log.error("Invalid verification ID issue");
				return responseCache.getOrDefault("invalid_verification_id", responseCache.get("default"));
			}

			if ("insufficient_balance".equalsIgnoreCase(code)) {
				log.error("Invalid verification ID issue");
				return responseCache.getOrDefault("insufficient_balance", responseCache.get("default"));
			}

		}
		if (TYPE_RATE_LIMITE_ERROR.equalsIgnoreCase("too_many_requests_per_operation")) {
			log.error("Invalid verification ID issue");
			return responseCache.getOrDefault("too_many_requests_per_operation", responseCache.get("default"));
		}
		if (internal_error.equalsIgnoreCase("internal_error")) {
			log.error("Invalid verification ID issue");
			return responseCache.getOrDefault("internal_error", responseCache.get("default"));
		}
		// Fallback to default
		return responseCache.get("default");
	}

	@Override
	public ResponseModel DlVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId,
			Long productRate,String orderId) {
		log.info("DlVerifyResponseToMerchant{}:"+orderId);
		String timestamp = ISO_FORMATTER.format(Instant.now());
		String refId = responseObj.optString("reference_id", "");
		String status = responseObj.optString("status", "").toUpperCase();
		log.info("Staus DL Response" + status);
		String type = responseObj.optString("type", "");
		String code = responseObj.optString("code", "");
		log.info("cdoe From cashfree"+code);
		String message = responseObj.optString("message", "");
		log.info("Dl verification processing for trackId: {}, status: {}, type: {}, code: {}", trackId, status, type,
				code);
		DlResponseToMerchant baseResponse = getCachedResponse(status, type, code);
		boolean isValid = STATUS_VALID.equals(status);
		String ResonMessage;
		if(!isValid)
		{
			ResonMessage=message;
		}else
		{
			ResonMessage=status;
		}
		String billable = isValid ? "Y" : "N";
		String responseStatus = isValid ? "success" : "failed";
		String Validstatus = isValid ? "VALID" : "INVALID"; // ✅ Corrected
		log.info("Code with Status: " + code + " => " + Validstatus);
		int responseCode = isValid ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
		if (isValid) {
			log.info("Dl Success Valid deduct balance",productRate);
			updateBalance.updateWalletBalance(merchantId, productRate);
		}
		DlResponseToMerchant responseToMerchant;
		if (isValid) {
			JSONObject jsonObjectDetails = responseObj.getJSONObject("details_of_driving_licence");
			JSONObject jsonObjectAddress = jsonObjectDetails.getJSONObject("split_address");
			log.info("JSON Object Split Address: {}", jsonObjectAddress);
			// Arrays
			JSONArray countryArray = jsonObjectAddress.getJSONArray("country");
			JSONArray districtArray = jsonObjectAddress.getJSONArray("district");
			JSONArray stateArray = jsonObjectAddress.getJSONArray("state");
			JSONArray cityArray = jsonObjectAddress.getJSONArray("city");
			// Extract values
			String countryName = countryArray.getString(1); // e.g., "INDIA"
			String district = districtArray.getString(0);
			String state = stateArray.getJSONArray(0).getString(0); // e.g., "KARNATAKA"
			String city = cityArray.getString(0);
			String pincode = jsonObjectAddress.optString("pincode", "");
			DlResponseToMerchant.DlResponseToMerchantBuilder builder = baseResponse.toBuilder()
					.dl_number(responseObj.optString("dl_number", ""))
					.dl_validity(responseObj.optString("dl_validity", "")).status(Validstatus)
					.date_of_issue(jsonObjectDetails.optString("date_of_issue", ""))
					.name(jsonObjectDetails.optString("name", ""))
					.father_or_husband_name(jsonObjectDetails.optString("father_or_husband_name", ""))
					.user_image(jsonObjectDetails.optString("photo", ""))
					.complete_address(jsonObjectDetails.optString("address", ""))
					.cov_details(
							ToSafeStringMapList.covSafeStringMapList(jsonObjectDetails.optJSONArray("cov_details")))
					.district(district).state(state).city(city).pincode(pincode).country(countryName)
					.reference_id(refId).track_id(trackId).merchant_id(merchantId).response_timestamp(timestamp)
					.billable(billable);
					//.order_id(orderId);
			        
			responseToMerchant = builder.build();
		} else {
			log.info("DL Failed Inside"+orderId);
			// Minimal keys only for failure - no personal info keys
			DlResponseToMerchant.DlResponseToMerchantBuilder builder = baseResponse.toBuilder()
					// .reference_id(refId)
					.track_id(trackId).merchant_id(merchantId)
					.response_timestamp(timestamp)
					.response_code(baseResponse.getResponse_code())
					.response_message(baseResponse.getResponse_message())
					.billable(billable)
					.success(baseResponse.getSuccess());
			       // .order_id(orderId);
			responseToMerchant = builder.build();
		}
		log.info("DL verification response to merchant:{}", responseToMerchant);
		dlResponseUpdate.updateDlResponse(trackId, responseToMerchant, responseObj, isValid, refId, productRate,ResonMessage);
		return new ResponseModel(responseStatus, responseCode, responseToMerchant);
	}

	

}
