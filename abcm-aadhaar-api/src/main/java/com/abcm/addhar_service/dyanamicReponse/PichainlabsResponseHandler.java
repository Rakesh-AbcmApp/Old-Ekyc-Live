package com.abcm.addhar_service.dyanamicReponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.abcm.addhar_service.dto.ResponseModel;
import com.abcm.addhar_service.merchantReponseDto.OtpSendReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpVerifyResponseToMerchant;
import com.abcm.addhar_service.util.AadharResponseUpdate;
import com.abcm.addhar_service.util.PichainChargebelUtil;
import com.abcm.addhar_service.util.UpdateBalance;
import com.abcmkyc.entity.KycData;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PichainlabsResponseHandler implements ProviderResponseHandler<ResponseModel> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);
    private final Environment environment;
    private final UpdateBalance updateBalance;
    private final PichainChargebelUtil  pichainChargebelUtil;
    private final AadharResponseUpdate aadharResponseUpdate;
    private final Map<String, OtpSendReponseToMerchnat> otpSendResponseCache = new ConcurrentHashMap<>();
    private final Map<String, OtpVerifyResponseToMerchant> otpVerifyResponseCache = new ConcurrentHashMap<>();
    public PichainlabsResponseHandler(Environment environment, UpdateBalance updateBalance,AadharResponseUpdate aadharResponseUpdate,PichainChargebelUtil  pichainChargebelUtil) {
        this.environment = environment;
        this.updateBalance = updateBalance;
        this.aadharResponseUpdate=aadharResponseUpdate;
        this.pichainChargebelUtil= pichainChargebelUtil;
    }
    @PostConstruct
    private void init() {
    	
        // Initialize OTP Send response cache
        otpSendResponseCache.put("2200|Offline Aadhaar initiated", buildOtpSendResponse("otp-send-success", true));
        
        /*Invalid request Body b-yes*/
        otpSendResponseCache.put("4003|Offline Aadhar Process Failed - Aadhar Number Not Found", buildOtpSendResponse("aadhaar-format-validation", false));
       
        /*Invalid addhar No b-yes*/
        otpSendResponseCache.put("2201|Offline Aadhar Process Failed - Aadhar Number Not Found", buildOtpSendResponse("aadhaar-format-validation", false));
      
        /*not linked mobile to adhar b-yes*/
        otpSendResponseCache.put("2201|Offline Aadhaar Initiate Failed :Aadhaar number not linked to mobile number", buildOtpSendResponse("aadhar-not-linked-mobile", false));

        otpSendResponseCache.put("4004|Offline Aadhar Process Failed - Aadhar Number Not Found", buildOtpSendResponse("aadhaar-format-validation", false));
        
        /*invalid orgId b-yes*/
        otpSendResponseCache.put("4001|Invalid Request, required Valid OrgId", buildOtpSendResponse("aadhaar-format-validation", false));
 
        otpSendResponseCache.put("4015|No response received from source, please try again later", buildOtpSendResponse("porvider-blc-low", false));
        
        otpSendResponseCache.put("2203|Offline Aadhar Process Failed - No Response From Source, Please Re-initiate a fresh request", buildOtpSendResponse("porvider-blc-low", false));

        
        otpSendResponseCache.put("default", buildOtpSendResponse("otp-send-failed", false));

        
        
        
        // Initialize OTP Verify response cache
        otpVerifyResponseCache.put("4003|Invalid Request/reference_id, required reference_id", buildOtpVerifyResponse("request-id-not-found", false));
        
        otpVerifyResponseCache.put("4003|Offline Aadhaar process failed: Invalid Reference ID", buildOtpVerifyResponse("request-id-not-found", false));
        
        otpVerifyResponseCache.put("4003|Offline Aadhar Process Failed - Source Session Expired, Please Re-initiate a fresh request", buildOtpVerifyResponse("otp-expired-res", false));
        
        otpVerifyResponseCache.put("2203|Offline Aadhar Process Failed - Invalid OTP", buildOtpVerifyResponse("otp-validation", false));
        
        
        otpVerifyResponseCache.put("2264|Incorrect OTP", buildOtpVerifyResponse("otp-validation", false));
        
        otpVerifyResponseCache.put("2202|Offline Aadhaar is Succeed.", buildOtpVerifyResponse("otp-verify-massage", true));
        
        otpVerifyResponseCache.put("4015|No response received from source, please try again later", buildOtpVerifyResponse("porvider-blc-low", false));
        
        otpVerifyResponseCache.put("2203|Offline Aadhar Process Failed - No Response From Source, Please Re-initiate a fresh request", buildOtpVerifyResponse("porvider-blc-low", false));

       
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
    public ResponseModel OTPSend(JSONObject input, String trackId, String merchantId, Long productRate, String orderId) {
        try {
            log.info("Pichainlabs Dispatcher to Handler response: {}, OrderId: {}",input);
            
            String requestId = input.optString("reference_id");
            
            JSONObject statusObject = input.getJSONObject("status");
            
            String responseCode = statusObject.optString("statusCode", "").trim();
            
            String refId = statusObject.optString("ref_id");
            
            String statusMessage = statusObject.optString("statusMessage", "").trim();
            
            String timestamp = ISO_FORMATTER.format(Instant.now());
            
            String compositeKey = responseCode + "|" + statusMessage;
            
            OtpSendReponseToMerchnat baseResponse = otpSendResponseCache.getOrDefault(
                compositeKey,
                otpSendResponseCache.getOrDefault(responseCode, otpSendResponseCache.get("default"))
            );
            
            boolean success = responseCode.equals("2200") &&
                              "Offline Aadhaar initiated".equalsIgnoreCase(statusMessage);
            int httpStatus = success ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
            OtpSendReponseToMerchnat.OtpSendReponseToMerchnatBuilder builder = baseResponse.toBuilder()
                .merchant_id(merchantId)
                .track_id(trackId)
                .response_timestamp(timestamp);
                //.order_id(orderId);
            if (baseResponse.isSuccess()) {
                builder.request_id(requestId);
            }
            
            
            
           /* chnarges deduction utilti method used code and massage pichain labs*/
            
            
            try {
                log.info("Status Code: {}", responseCode);
                boolean isChargeable = pichainChargebelUtil.isChargeable(responseCode,statusMessage);
                log.info("Is chargeable? {}", isChargeable);
                if (isChargeable) {
                    builder.billable("Y");
                    updateBalance.updateWalletBalanceAfterOtp(merchantId, productRate);
                    log.info("Balance deducted for merchant: {}", merchantId);
                } else {
                    builder.billable("N");
                    log.info("Not chargeable. Skipping balance deduction.");
                }

            } catch (Exception e) {
                log.error("Error while deducting balance for merchant: {}", merchantId, e);
            }
            
            OtpSendReponseToMerchnat responseToMerchant = builder.build();
            aadharResponseUpdate.updateOtpSendResponse(
                trackId,
                responseToMerchant,
                input,
                responseToMerchant.isSuccess(),
                requestId,
                statusMessage
            );
            log.info("After update response send otp in DB");
            return new ResponseModel(success ? "success" : "failed", httpStatus, responseToMerchant);

        } catch (Exception e) {
            log.error("Exception while processing OTPSend for orderId: {}", trackId, e);
        }

        return null;
    }


    
    
    
    
    
    
    
    
    @Override
    public ResponseModel OTPVerify(JSONObject input,String mid, Long productrate,KycData kycData) {
    	try {
    		log.info("Pichainlabs Dispatcher Handler response otp verify : {}, OrderId: {}",input,"track id",kycData.getTrackId());
    		String timestamp = ISO_FORMATTER.format(Instant.now());
        	JSONObject jsonObjectstatus=input.getJSONObject("status");
            String responseCode = jsonObjectstatus.optString("statusCode");
            String ref_id=null;
            ref_id = jsonObjectstatus.optString("ref_id");
            String statusMessage = jsonObjectstatus.optString("statusMessage");
            String compositeKey = responseCode + "|" + statusMessage;
            log.info("compositeKey---------------------"+compositeKey);
            OtpVerifyResponseToMerchant baseResponse = otpVerifyResponseCache.getOrDefault(
                    compositeKey,
                    otpVerifyResponseCache.getOrDefault(responseCode, otpVerifyResponseCache.get("default")));
            OtpVerifyResponseToMerchant.OtpVerifyResponseToMerchantBuilder builder = baseResponse.toBuilder()
                    .track_id(kycData.getTrackId())
                    .merchant_id(kycData.getMerchantId())
                    .response_timestamp(timestamp)
                    .response_code(baseResponse.getResponse_code())
                    .response_message(baseResponse.getResponse_message())
                    .success(baseResponse.isSuccess());
                    //.order_id(kycData.getOrderId());
            if (baseResponse.isSuccess()) {
                builder.referenceId(ref_id);
            }
            boolean success =
            	    "2202".equals(responseCode != null ? responseCode.trim() : "") &&
            	    "Offline Aadhaar is Succeed.".equalsIgnoreCase(statusMessage != null ? statusMessage.trim() : "");
                  builder.billable("N");
            //builder.billable("N");
            JSONObject result = input.optJSONObject("data");
            if (result != null) {
                builder.userFullName(result.optString("name"))
                        .userParentName(result.optString("user_parent_name"))
                        .userAadhaarNumber(result.optString("masked_aadhar"))
                        .userDob(result.optString("date_of_birth"))
                        .userGender(result.optString("gender"))
                        .userProfileImage(result.optString("photo_link"));
                         
                 JSONObject address = result.optJSONObject("split_address");
                if (address != null) {
                    builder.house(address.optString("house"))
                            .street(address.optString("street"))
                            .landmark(address.optString("landmark"))
                            .loc(address.optString("loc"))
                            .addressZip(address.optString("pincode"))
                            .po(address.optString("po"))
                            .dist(address.optString("dist"))
                            .subdist(address.optString("subdist"))
                            .vtc(address.optString("vtc"))
                            .state(address.optString("state"))
                            .country(address.optString("country"));
                     }
            }
            OtpVerifyResponseToMerchant response = builder.build();
            aadharResponseUpdate.updateOtpVerifyResponse( response, input, response.isSuccess(),ref_id,productrate,statusMessage,kycData);
            log.info("After aadharResponseUpdate and continue..{}");
            int httpStatus =
            	    "2202".equals(responseCode) && "Offline Aadhaar is Succeed.".equals(statusMessage)
            	        ? HttpStatus.OK.value()
            	        : HttpStatus.BAD_REQUEST.value();

            	return new ResponseModel(
            	    "2202".equals(responseCode) && "Offline Aadhaar is Succeed.".equals(statusMessage) ? "success" : "failed",
            	    httpStatus,
            	    response
            	);	
    	}catch (Exception e) {
			log.error("exception while otp verify pichnainlabs provider", e.getMessage());
		}
		return null;
    	 
    }
    
    
    
    
    
	
	@Override
	public ResponseModel digiVerify(JSONObject input, String trackId, String merchantId,String orderId) {
		// TODO Auto-generated method stub
		return null;
	}
	

	

  
}
