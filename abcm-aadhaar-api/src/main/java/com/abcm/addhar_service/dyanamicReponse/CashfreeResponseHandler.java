package com.abcm.addhar_service.dyanamicReponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.merchantReponseDto.OtpSendReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpVerifyResponseToMerchant;
import com.abcm.addhar_service.util.AadharResponseUpdate;
import com.abcm.addhar_service.util.UpdateBalance;
import com.abcmkyc.entity.KycData;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CashfreeResponseHandler implements ProviderResponseHandler<ResponseModel> {

	@Autowired
	private Environment environment;
	@Autowired
	private  AadharResponseUpdate aadharResponseUpdate;
	
	
	
	@Autowired
	UpdateBalance balance;

	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneOffset.UTC);

	private final Map<String, OtpSendReponseToMerchnat> otpSendResponseCache = new ConcurrentHashMap<>();
	private final Map<String, OtpVerifyResponseToMerchant> otpVerifyResponseCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		// Initialize OTP send response cache
		otpSendResponseCache.put("SUCCESS", buildOtpSendResponse("otp-send-success", true));
		otpSendResponseCache.put("aadhaar_invalid", buildOtpSendResponse("aadhaar-format-validation", false));
		otpSendResponseCache.put("authentication_failed", buildOtpSendResponse("invalid-client", false));
		otpSendResponseCache.put("aadhaar_empty", buildOtpSendResponse("aadhaar-format-validation", false));
		otpSendResponseCache.put("verification_pending", buildOtpSendResponse("otp-send-failed", false));
		otpSendResponseCache.put("insufficient_balance", buildOtpSendResponse("porvider-blc-low", false));
		otpSendResponseCache.put("default", buildOtpSendResponse("otp-send-failed", false));

		// Initialize OTP verify response cache
		otpVerifyResponseCache.put("VALID", buildOtpVerifyResponse("otp-verify-massage", true));
		otpVerifyResponseCache.put("otp_invalid", buildOtpVerifyResponse("otp-validation", false));
		otpVerifyResponseCache.put("verification_failed", buildOtpVerifyResponse("otp-expired-res", false));
		otpVerifyResponseCache.put("invalid_ref_id", buildOtpVerifyResponse("request-id-validation", false));
		otpVerifyResponseCache.put("ref_id_missing", buildOtpVerifyResponse("request-id-validation", false));
		otpVerifyResponseCache.put("otp_empty", buildOtpVerifyResponse("otp-validation", false));
		otpVerifyResponseCache.put("insufficient_balance", buildOtpVerifyResponse("porvider-blc-low", false));
		otpVerifyResponseCache.put("default", buildOtpVerifyResponse("otp-verify-failed", false));
	}

	private OtpSendReponseToMerchnat buildOtpSendResponse(String keyPrefix, boolean success) {
		return OtpSendReponseToMerchnat.builder()
				.response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix))
				.success(success)
				.build();
	}

	private OtpVerifyResponseToMerchant buildOtpVerifyResponse(String keyPrefix, boolean success) {
		return OtpVerifyResponseToMerchant.builder()
				.response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix))
				.success(success)
				.build();
	}

	
	private OtpSendReponseToMerchnat getOtpSendResponse(String status, String type, String code) {
		if ("SUCCESS".equalsIgnoreCase(status)) {
			return otpSendResponseCache.getOrDefault("SUCCESS", otpSendResponseCache.get("default"));
		}
		if ("validation_error".equalsIgnoreCase(type) && "aadhaar_invalid".equalsIgnoreCase(code)) {
			return otpSendResponseCache.getOrDefault("aadhaar_invalid", otpSendResponseCache.get("default"));
		}
		
		if ("validation_error".equalsIgnoreCase(type) && "aadhaar_empty".equalsIgnoreCase(code)) {
			return otpSendResponseCache.getOrDefault("aadhaar_empty", otpSendResponseCache.get("default"));
		}
		
		
		if ("validation_error".equalsIgnoreCase(type) && "verification_pending".equalsIgnoreCase(code)) {
			return otpSendResponseCache.getOrDefault("verification_pending", otpSendResponseCache.get("default"));
		}
		

		if ("validation_error".equalsIgnoreCase(type) && "insufficient_balance".equalsIgnoreCase(code)) {
			return otpSendResponseCache.getOrDefault("insufficient_balance", otpSendResponseCache.get("default"));
		}
		if ("authentication_error".equalsIgnoreCase(type) && "authentication_failed".equalsIgnoreCase(code)) {
			return otpSendResponseCache.getOrDefault("authentication_failed", otpSendResponseCache.get("default"));
		}
		return otpSendResponseCache.get("default");

		//{"code":"verification_failed","type":"validation_error","message":"Otp expired","error":{"refId":"140334"}}
	}

	
	private OtpVerifyResponseToMerchant getOtpVerifyResponse(String status, String type, String code) {
		if ("VALID".equalsIgnoreCase(status)) {
			return otpVerifyResponseCache.getOrDefault("VALID", otpVerifyResponseCache.get("default"));
		}
		if ("validation_error".equalsIgnoreCase(type) && "aadhaar_invalid".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("aadhaar_invalid", otpVerifyResponseCache.get("default"));
		}
		if ("authentication_error".equalsIgnoreCase(type) && "authentication_failed".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("authentication_failed", otpVerifyResponseCache.get("default"));
		}
		if("validation_error".equalsIgnoreCase(type) && "otp_invalid".equalsIgnoreCase(code))
		{
			return otpVerifyResponseCache.getOrDefault("otp_invalid", otpVerifyResponseCache.get("default"));

		}
		if ("validation_error".equalsIgnoreCase(type) && "verification_failed".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("verification_failed", otpVerifyResponseCache.get("default"));
		}
		
		if ("validation_error".equalsIgnoreCase(type) && "invalid_ref_id".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("invalid_ref_id", otpVerifyResponseCache.get("default"));
		}
		
		if ("validation_error".equalsIgnoreCase(type) && "ref_id_missing".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("ref_id_missing", otpVerifyResponseCache.get("default"));
		}
		if ("validation_error".equalsIgnoreCase(type) && "otp_empty".equalsIgnoreCase(code)) {
			log.info("Opt null not in request");
			return otpVerifyResponseCache.getOrDefault("otp_empty", otpVerifyResponseCache.get("default"));
		}
		
		if ("validation_error".equalsIgnoreCase(type) && "insufficient_balance".equalsIgnoreCase(code)) {
			return otpVerifyResponseCache.getOrDefault("insufficient_balance", otpVerifyResponseCache.get("default"));
		}

		return otpVerifyResponseCache.get("default");
	}

	@Override
	public ResponseModel OTPSend(JSONObject input, String trackId, String merchantId,Long productrate,String orderID) {
		String timestamp = ISO_FORMATTER.format(Instant.now());
		String refId = input.optString("ref_id");
		String type = input.optString("type", "").toLowerCase();
		String code = input.optString("code", "").toLowerCase();
		String message = input.optString("message", "").toLowerCase();
		String status = input.optString("status", "").toUpperCase();
		boolean success = "SUCCESS".equalsIgnoreCase(status);
			String ReasonMassage;
			if(!success)
			{
				ReasonMassage=input.optString("message", "").toUpperCase();
			}
			else
			{
				ReasonMassage=input.optString("message", "").toUpperCase();
			}
		log.info("OTPSend processing for trackId: {}, status: {}, type: {}, code: {}", trackId, status, type, code);
		OtpSendReponseToMerchnat baseResponse = getOtpSendResponse(status, type, code);
		OtpSendReponseToMerchnat finalResponse = baseResponse.toBuilder()
				.merchant_id(merchantId)
				.request_id(refId)
				.track_id(trackId)
				.response_timestamp(timestamp)
				//.order_id(orderID)
				.build();
		aadharResponseUpdate.updateOtpSendResponse(trackId, finalResponse, input, baseResponse.isSuccess(),refId,ReasonMassage);
		String responseStatus = baseResponse.isSuccess() ? "Success" : "Failed";
		int responseCode = baseResponse.isSuccess() ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
		return new ResponseModel(responseStatus, responseCode, finalResponse);
	}
	
	public ResponseModel OTPVerify(JSONObject responseObj, String merchantId, Long productrate, KycData kycData) {
	    final String timestamp = ISO_FORMATTER.format(Instant.now());
	    final String refId = responseObj.optString("ref_id", "");
	    final String status = responseObj.optString("status", "").toUpperCase();
	    final String type = responseObj.optString("type", "").toLowerCase();
	    final String code = responseObj.optString("code", "").toLowerCase();
	    boolean success = "VALID".equalsIgnoreCase(status);
		String ReasonMassage;
		if(!success)
		{
			ReasonMassage=responseObj.optString("message", "").toUpperCase();
		}
		else
		{
			ReasonMassage=responseObj.optString("message", "").toUpperCase();
		}
	    log.info("OTPVerify processing for trackId: {}, status: {}, type: {}, code: {}", kycData.getTrackId(), status, type, code);
	    OtpVerifyResponseToMerchant baseResponse = getOtpVerifyResponse(status, type, code);
	    JSONObject splitAddress = responseObj.optJSONObject("split_address");
	    boolean isValid = "VALID".equalsIgnoreCase(status);
	    boolean isSuccess = baseResponse.isSuccess();
	    OtpVerifyResponseToMerchant.OtpVerifyResponseToMerchantBuilder responseBuilder = baseResponse.toBuilder()
	            .track_id(nonNull(kycData.getTrackId()))
	            .merchant_id(kycData.getMerchantId())
	            .response_timestamp(nonNull(timestamp));
	            //.order_id(kycData.getOrderId());
	    if (isValid) {
	        responseBuilder.referenceId(refId);
	    }
	    if (isSuccess) {
	        // Cache frequently accessed fields
	        String name = nonNull(responseObj.optString("name", ""));
	        String careOf = nonNull(responseObj.optString("care_of", ""));
	        String dob = nonNull(responseObj.optString("dob", ""));
	        String gender = nonNull(responseObj.optString("gender", ""));
	        String photoLink = nonNull(responseObj.optString("photo_link", ""));
	        responseBuilder
	            .billable(isValid ? "Y" : "N")
	            .userFullName(name)
	            .userParentName(careOf)
	            .userAadhaarNumber("")
	            .userDob(dob)
	            .userGender(gender)
	            .userProfileImage(photoLink)
	            .house(nonNull(getJsonField(splitAddress, "house")))
	            .street(nonNull(getJsonField(splitAddress, "street")))
	            .landmark(nonNull(getJsonField(splitAddress, "landmark")))
	            .loc(nonNull(getJsonField(splitAddress, "locality")))
	            .po(nonNull(getJsonField(splitAddress, "po")))
	            .dist(nonNull(getJsonField(splitAddress, "dist")))
	            .subdist(nonNull(getJsonField(splitAddress, "subdist")))
	            .vtc(nonNull(getJsonField(splitAddress, "vtc")))
	            .state(nonNull(getJsonField(splitAddress, "state")))
	            .country(nonNull(getJsonField(splitAddress, "country")))
	            .addressZip(nonNull(getJsonField(splitAddress, "pincode")));
	    }
	    OtpVerifyResponseToMerchant responseToMerchant = responseBuilder.build();
	    log.info("OTPVerify response: {Success to merchnat}");
	    aadharResponseUpdate.updateOtpVerifyResponse(responseToMerchant, responseObj, isSuccess, refId, productrate,ReasonMassage,kycData);
	    log.info("After Update Response DB{}");
	    if (isValid) {
	        balance.updateWalletBalanceAfterOtp(kycData.getMerchantId(), productrate);
	        log.info("Balance updated for merchantId: {}", kycData.getMerchantId());
	        return new ResponseModel("Success", HttpStatus.OK.value(), responseToMerchant);
	    }
	    log.info("After Balance Update");
	    return new ResponseModel("Failed", HttpStatus.BAD_REQUEST.value(), responseToMerchant);
	    
	}

	private String nonNull(String value) {
	    return value == null ? "" : value;
	}

	
	private String getJsonField(JSONObject obj, String key) {
		return obj != null ? obj.optString(key, null) : null;
	}

	@Override
	public ResponseModel digiVerify(JSONObject input, String trackId, String merchantId,String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	

	


}
