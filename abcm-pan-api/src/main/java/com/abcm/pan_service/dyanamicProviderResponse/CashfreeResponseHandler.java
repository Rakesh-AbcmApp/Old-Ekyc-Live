package com.abcm.pan_service.dyanamicProviderResponse;

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

import com.abcm.pan_service.dto.PanResponseToMerchant;
import com.abcm.pan_service.dto.ResponseModel;
import com.abcm.pan_service.util.PanResponseUpdate;
import com.abcm.pan_service.util.UpdateBalance;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CashfreeResponseHandler implements ProviderResponseHandler<ResponseModel> {

    @Autowired
    private Environment environment;

    @Autowired
    private PanResponseUpdate panResponseUpdate;

    @Autowired
    private UpdateBalance updateBalance;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);

    private static final String STATUS_VALID = "VALID";
    private static final String TYPE_VALIDATION_ERROR = "validation_error";
    private static final String TYPE_RATE_LIMITE_ERROR="rate_limit_error";
    private static final String internal_error="internal_error";

    private final Map<String, PanResponseToMerchant> responseCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        // Initialize predefined responses
        responseCache.put(STATUS_VALID, buildResponse("pan-validation-msg", true));
        responseCache.put("pan_length_short", buildResponse("PAN-format-validation", false));
        responseCache.put("invalid_verification_id", buildResponse("verification-id-msg", false));
        responseCache.put("insufficient_balance", buildResponse("porvider-blc-low", false));
        responseCache.put("too_many_requests_per_operation", buildResponse("porvider-blc-low", false));
        responseCache.put("internal_error", buildResponse("pan-validation-failed-msg", false));
        responseCache.put("default", buildResponse("pan-validation-failed-msg", false));
    }

    private PanResponseToMerchant buildResponse(String keyPrefix, boolean success) {
        return PanResponseToMerchant.builder()
                .response_code(environment.getProperty("custom.codes." + keyPrefix))
                .response_message(environment.getProperty("custom.messages." + keyPrefix))
                .success(success)
                .build();
    }

    private PanResponseToMerchant getCachedResponse(String status, String type, String code) {
        if (STATUS_VALID.equalsIgnoreCase(status)) {
            return responseCache.getOrDefault(STATUS_VALID, responseCache.get("default"));
        }

        if (TYPE_VALIDATION_ERROR.equalsIgnoreCase(type)) {
            if ("pan_length_short".equalsIgnoreCase(code)) {
                log.error("PAN format issue detected");
                return responseCache.getOrDefault("pan_length_short", responseCache.get("default"));
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
        if(TYPE_RATE_LIMITE_ERROR.equalsIgnoreCase("too_many_requests_per_operation"))
        {
        	  log.error("Invalid verification ID issue");
              return responseCache.getOrDefault("too_many_requests_per_operation", responseCache.get("default"));
        }
        
        if(internal_error.equalsIgnoreCase("internal_error"))
        {
        	  log.error("Invalid verification ID issue");
              return responseCache.getOrDefault("internal_error", responseCache.get("default"));
        }

        // Fallback to default
        return responseCache.get("default");
    }
    @Override
    public ResponseModel PanVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId, Long productRate,String orderId) {
        String timestamp = ISO_FORMATTER.format(Instant.now());
        String refId = responseObj.optString("reference_id", "");
        String status = responseObj.optString("status", "").toUpperCase();
        String type = responseObj.optString("type", "");
        String code = responseObj.optString("code", "");
        String message = responseObj.optString("message", "");
        boolean isValidStatus = STATUS_VALID.equals(status);
		String ResonMessage;
		if(!isValidStatus)
		{
			ResonMessage=message;
		}else
		{
			ResonMessage=status;
		}

        log.info("PAN verification processing for trackId: {}, status: {}, type: {}, code: {}", trackId, status, type, code);

        PanResponseToMerchant baseResponse = getCachedResponse(status, type, code);

        boolean isValid = STATUS_VALID.equals(status) && "PAN verified successfully".equalsIgnoreCase(message);
        String billable = isValid ? "Y" : "N";

        String responseStatus = isValid ? "success" : "failed";
        int responseCode = isValid ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();

        if (isValid) {
            updateBalance.updateWalletBalance(merchantId, productRate);
        }

        PanResponseToMerchant responseToMerchant;

        if (isValid) {
            // Extract user address if present
            PanResponseToMerchant.UserAddress userAddress = extractUserAddress(responseObj.optJSONObject("address"));

            PanResponseToMerchant.PanResponseToMerchantBuilder builder = baseResponse.toBuilder()
                .reference_id(refId)
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .pan_number(emptyIfNull(responseObj.optString("pan")))
                .user_full_name(emptyIfNull(responseObj.optString("name_pan_card")))
                .user_email(emptyIfNull(responseObj.optString("email")))
                .user_phone_number(emptyIfNull(responseObj.optString("mobile_number")))
                .user_dob(emptyIfNull(responseObj.optString("date_of_birth", "")))
                .user_gender(emptyIfNull(responseObj.optString("gender")))
                .masked_aadhaar(emptyIfNull(responseObj.optString("masked_aadhaar_number")))
                .billable(billable)
                .user_address(userAddress)
                .aadhaar_linked_status(responseObj.optBoolean("aadhaar_linked", false));

            responseToMerchant = builder.build();

        } else {
            // Minimal keys only for failure - no personal info keys
            PanResponseToMerchant.PanResponseToMerchantBuilder builder = baseResponse.toBuilder()
               // .reference_id(refId)
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
                .billable(billable)
                .success(baseResponse.getSuccess());
                //.order_id(orderId);
            responseToMerchant = builder.build();
        }
        log.info("PAN verification response: {}", responseToMerchant);
        panResponseUpdate.updatePanResponse(trackId, responseToMerchant, responseObj, isValid, refId, productRate,ResonMessage);
        return new ResponseModel(responseStatus, responseCode, responseToMerchant);
    }
    /** Utility to convert null to empty string for safe display in response */
    private String emptyIfNull(String value) {
        return value == null ? "" : value;
    }
    private PanResponseToMerchant.UserAddress extractUserAddress(JSONObject addressObj) {
        if (addressObj == null) return null;
        PanResponseToMerchant.UserAddress userAddress = new PanResponseToMerchant.UserAddress();
        userAddress.setCountry(emptyIfNull(addressObj.optString("country", "")));
        userAddress.setState(emptyIfNull(addressObj.optString("state", "")));
        userAddress.setCity(emptyIfNull(addressObj.optString("city", "")));
        userAddress.setPincode(emptyIfNull(addressObj.optString("pincode", "")));
        userAddress.setStreet(emptyIfNull(addressObj.optString("street", "")));
        userAddress.setFullAddress(emptyIfNull(addressObj.optString("full_address", "")));
        return userAddress;
    }

}
