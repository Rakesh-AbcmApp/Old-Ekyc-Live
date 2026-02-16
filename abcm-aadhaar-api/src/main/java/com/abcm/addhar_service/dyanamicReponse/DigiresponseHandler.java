package com.abcm.addhar_service.dyanamicReponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.merchantReponseDto.DigiReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpSendReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpVerifyResponseToMerchant;
import com.abcm.addhar_service.service.UrlShortenerService;
import com.abcm.addhar_service.util.AadharResponseUpdate;
import com.abcm.addhar_service.util.TimestampUtil;
import com.abcm.addhar_service.util.UpdateBalance;
import com.abcmkyc.entity.KycData;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class DigiresponseHandler implements ProviderResponseHandler<ResponseModel> {
	
	
	@Autowired
    private UrlShortenerService urlShortenerService;
    
     @Value("${ContextPath}")
     public String connectionUrl;
	

	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
	.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	.withZone(ZoneOffset.UTC);
private final Environment environment;
private final UpdateBalance updateBalance;
private final AadharResponseUpdate aadharResponseUpdate;
private final Map<String, OtpSendReponseToMerchnat> otpSendResponseCache = new ConcurrentHashMap<>();
private final Map<String, OtpVerifyResponseToMerchant> otpVerifyResponseCache = new ConcurrentHashMap<>();
public DigiresponseHandler(Environment environment, UpdateBalance updateBalance,AadharResponseUpdate aadharResponseUpdate) {
	this.environment = environment;
	this.updateBalance = updateBalance;
	this.aadharResponseUpdate=aadharResponseUpdate;
}
@PostConstruct
private void init() {
	// Initialize OTP Send response cache
	otpSendResponseCache.put("100", buildOtpSendResponse("otp-send-success", true));
	otpSendResponseCache.put("104", buildOtpSendResponse("consent-validation", false));
	otpSendResponseCache.put("105", buildOtpSendResponse("aadhaar-format-validation", false));
	otpSendResponseCache.put("106", buildOtpSendResponse("aadhaar-format-validation", false));
	otpSendResponseCache.put("112", buildOtpSendResponse("provider-invalid-res", false));
	otpSendResponseCache.put("113", buildOtpSendResponse("provider-invalid-res", false));
	otpSendResponseCache.put("default", buildOtpSendResponse("otp-send-failed", false));


	// Initialize OTP Verify response cache
	otpVerifyResponseCache.put("106|Request ID is invalid", buildOtpVerifyResponse("request-id-validation", false));
	otpVerifyResponseCache.put("106", buildOtpVerifyResponse("otp-validation", false));
	otpVerifyResponseCache.put("107", buildOtpVerifyResponse("duplicate-txn", false));
	otpVerifyResponseCache.put("100", buildOtpVerifyResponse("otp-verify-massage", true));
	otpVerifyResponseCache.put("112", buildOtpVerifyResponse("porvider-blc-low", false));
	otpVerifyResponseCache.put("113", buildOtpVerifyResponse("porvider-blc-low", false));
	otpVerifyResponseCache.put("default", buildOtpVerifyResponse("otp-verify-failed", false)); // Added default explicitly
}

// Cache base response objects for reuse to reduce memory pressure
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

@Override
public ResponseModel OTPSend(JSONObject input, String trackId, String merchantId,Long productrate,String orderId) {
	log.info("Zoop Dispatcher to Handler response: {}", input);
	String responseCode = input.optString("response_code");
	String requestId = input.optString("request_id");
	String timestamp = ISO_FORMATTER.format(Instant.now());
	OtpSendReponseToMerchnat baseResponse = otpSendResponseCache.getOrDefault(responseCode, otpSendResponseCache.get("default"));
	boolean success = "100".equals(responseCode);
	int httpStatus = success ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
	OtpSendReponseToMerchnat.OtpSendReponseToMerchnatBuilder builder = baseResponse.toBuilder()
			.merchant_id(merchantId)
			.track_id(trackId)
			.response_timestamp(timestamp);
	        //.order_id(orderId);
	if (baseResponse.isSuccess()) {
		builder.request_id(requestId);
	}
	OtpSendReponseToMerchnat responseToMerchant = builder.build();
	aadharResponseUpdate.updateOtpSendResponse(trackId, responseToMerchant, input, responseToMerchant.isSuccess(),requestId,"");
	return new ResponseModel(success ? "success" : "failed", httpStatus, responseToMerchant);        
}



@Override
public ResponseModel OTPVerify(JSONObject input,String mid, Long productrate,KycData kycData) {
	String responseCode = input.optString("response_code");
	JSONObject metadata = input.optJSONObject("metadata");
	String requestId = input.optString("request_id");
	String timestamp = ISO_FORMATTER.format(Instant.now());
	String reasonMessage = metadata != null ? metadata.optString("reason_message") : "";
	String compositeKey = responseCode + "|" + reasonMessage;
	OtpVerifyResponseToMerchant baseResponse = otpVerifyResponseCache.getOrDefault(
			compositeKey,
			otpVerifyResponseCache.getOrDefault(responseCode, otpVerifyResponseCache.get("default")));
	OtpVerifyResponseToMerchant.OtpVerifyResponseToMerchantBuilder builder = baseResponse.toBuilder()
			.track_id(kycData.getTrackId())
			.merchant_id(mid)
			.response_timestamp(timestamp)
			.response_code(baseResponse.getResponse_code())
			.response_message(baseResponse.getResponse_message())
			.success(baseResponse.isSuccess());
	if (baseResponse.isSuccess()) {
		builder.referenceId(requestId);
	}
	if (metadata != null) {
		String billable = metadata.optString("billable");
		builder.billable(billable);
		if ("Y".equalsIgnoreCase(billable)) {

			updateBalance.updateWalletBalanceAfterOtp(mid, productrate);
			log.info("After Baklance update and continue..{}");
		}
	}
	JSONObject result = input.optJSONObject("result");
	if (result != null) {
		builder.userFullName(result.optString("user_full_name"))
		.userParentName(result.optString("user_parent_name"))
		.userAadhaarNumber(result.optString("user_aadhaar_number"))
		.userDob(result.optString("user_dob"))
		.userGender(result.optString("user_gender"))
		.userProfileImage(result.optString("user_profile_image"))
		.addressZip(result.optString("address_zip"));


		JSONObject address = result.optJSONObject("user_address");
		if (address != null) {
			builder.house(address.optString("house"))
			.street(address.optString("street"))
			.landmark(address.optString("landmark"))
			.loc(address.optString("loc"))
			.po(address.optString("po"))
			.dist(address.optString("dist"))
			.subdist(address.optString("subdist"))
			.vtc(address.optString("vtc"))
			.state(address.optString("state"))
			.country(address.optString("country"));
		}
	}
	OtpVerifyResponseToMerchant response = builder.build();
	aadharResponseUpdate.updateOtpVerifyResponse( response, input, response.isSuccess(),requestId,productrate,reasonMessage,kycData);
	log.info("After aadharResponseUpdate and continue..{}");
	int httpStatus = "100".equals(responseCode) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
	return new ResponseModel("100".equals(responseCode) ? "success" : "failed", httpStatus, response);

}






/*
 * @Override public ResponseModel digiVerify(JSONObject input, String trackId,
 * String merchantId) { System.out.println("digiVerify responseHandle: " +
 * input); log.info("digiVerify Dispatcher to Handler response: {}", input);
 * 
 * // Extract fields from Zoop response String requestId =
 * input.optString("request_id"); String sdkUrl = input.optString("sdk_url");
 * String expiresAt = input.optString("expires_at"); String webhookSecurityKey =
 * input.optString("webhook_security_key"); String requestTimestamp =
 * input.optString("request_timestamp"); boolean responseSuccess =
 * input.optBoolean("success"); // Current timestamp String timestamp =
 * ISO_FORMATTER.format(Instant.now()); // Determine response_code and
 * response_message String responseCode = responseSuccess ? "100" : "101";
 * String responseMessage = responseSuccess ?
 * "Digilocker link generated successfully" :
 * "Digilocker link generation failed"; // Build merchant response
 * DigiReponseToMerchnat responseToMerchant = DigiReponseToMerchnat.builder()
 * .merchant_id(merchantId) .track_id(trackId) .response_timestamp(timestamp)
 * .response_code(responseCode) .response_message(responseMessage)
 * .success(responseSuccess) .referenceId(requestId) .sdkUrl(sdkUrl)
 * .expiresAt(expiresAt) .webhookSecurityKey(webhookSecurityKey)
 * .requestTimestamp(requestTimestamp) .digilockerSuccess(responseSuccess)
 * .build(); // Persist response aadharResponseUpdate.updateDigiResponse(
 * trackId, responseToMerchant, input, responseSuccess, requestId,
 * webhookSecurityKey );
 * 
 * return new ResponseModel( responseSuccess ? "success" : "failed",
 * responseSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(),
 * responseToMerchant ); }
 */


@Override
public ResponseModel digiVerify(JSONObject input, String trackId, String merchantId,String orderId) {
    System.out.println("digiVerify responseHandle: " + input);
    log.info("digiVerify Dispatcher to Handler response: {}", input);

    boolean responseSuccess = input.optBoolean("success");

    // Always extract common fields
    String requestId = input.optString("request_id");
    String sdkUrl = input.optString("sdk_url");
    
    String shortCode=urlShortenerService.shorten(sdkUrl);
    String shortUrl=null;
    if (responseSuccess) {
   	 shortUrl = connectionUrl+"/api/v2/r/" + shortCode;
   }

    String expiresAt =   TimestampUtil.convertUtcToIst(input.optString("expires_at"));
    String webhookSecurityKey = input.optString("webhook_security_key");
    String requestTimestamp =  TimestampUtil.getISTTimestamp();

    // Optional: failed response might contain metadata
    JSONObject metadata = input.optJSONObject("metadata");
    String reasonMessage=null;
     reasonMessage = metadata != null ? metadata.optString("reason_message") : null;
    
    if (reasonMessage == null) {
        reasonMessage = (input.optString("response_message", null)); // your example
    }

    // Fallbacks if response failed
    String responseCode = responseSuccess
            ? "100"
                    : input.optString("response_code", "101");


    String responseMessage = responseSuccess
            ? "Digilocker link generated successfully"
                    : "Digilocker link generation failed";

    //String timestamp = ISO_FORMATTER.format(Instant.now());

    DigiReponseToMerchnat responseToMerchant = DigiReponseToMerchnat.builder()
            .merchant_id(merchantId)
            .track_id(trackId)
            .response_timestamp(requestTimestamp)
            .response_code(responseCode)
            .response_message(responseMessage)
            .success(responseSuccess)
            .referenceId(requestId)
            .sdkUrl(shortUrl)
            .expiresAt(expiresAt)
            .webhookSecurityKey(webhookSecurityKey)
            .requestTimestamp(requestTimestamp)
            .digilockerSuccess(responseSuccess)
            .order_id(orderId)
            .build();

    // Save full original response + parsed summary
    aadharResponseUpdate.updateDigiResponse(
            trackId,
            responseToMerchant,
            input,
            responseSuccess,
            requestId,
            webhookSecurityKey,
            reasonMessage,
            sdkUrl,
            shortUrl
            );

    return new ResponseModel(
            responseSuccess ? "success" : "failed",
                    responseSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(),
                            responseToMerchant
            );
}



}
