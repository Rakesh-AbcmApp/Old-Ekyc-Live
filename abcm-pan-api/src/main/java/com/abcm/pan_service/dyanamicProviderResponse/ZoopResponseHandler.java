package com.abcm.pan_service.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
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
public class ZoopResponseHandler implements ProviderResponseHandler<ResponseModel> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);

    private final Environment environment;
    private final UpdateBalance updateBalance;
    private final PanResponseUpdate panResponseUpdate;
    private final Map<String, PanResponseToMerchant> panVerifyResponseCache = new ConcurrentHashMap<>();
    public ZoopResponseHandler(Environment environment, UpdateBalance updateBalance,PanResponseUpdate panResponseUpdate) {
      
        this.environment = environment;
        this.updateBalance = updateBalance;
        this.panResponseUpdate=panResponseUpdate;
    }

    @PostConstruct
    private void init() {
        panVerifyResponseCache.put("100", buildPanVerifyResponse("pan-validation-msg", true));
        panVerifyResponseCache.put("104", buildPanVerifyResponse("pan-validation-failed-msg", false));
        panVerifyResponseCache.put("106|Invalid format for PAN number", buildPanVerifyResponse("PAN-format-validation", false));
        panVerifyResponseCache.put("106", buildPanVerifyResponse("pan-validation-failed-msg", false));
        panVerifyResponseCache.put("112", buildPanVerifyResponse("provider-invalid-res", false));
        panVerifyResponseCache.put("113", buildPanVerifyResponse("provider-invalid-res", false));
        panVerifyResponseCache.put("default", buildPanVerifyResponse("pan-validation-failed-msg", false));


    }
    private PanResponseToMerchant buildPanVerifyResponse(String keyPrefix, boolean success) {
        return PanResponseToMerchant.builder()
                .response_code(environment.getProperty("custom.codes." + keyPrefix))
                .response_message(environment.getProperty("custom.messages." + keyPrefix))
                .success(success)
                .build();
    }

 
    public ResponseModel PanVerifyResponseToMerchant(JSONObject input, String trackId, String merchantId, Long productRate,String orderId) {
    	log.info("Order Id"+orderId);
        String responseCode = input.optString("response_code");
        JSONObject metadata = input.optJSONObject("metadata");
        String requestId = input.optString("request_id");
        String timestamp = ISO_FORMATTER.format(Instant.now());
        String reasonMessage = metadata != null ? metadata.optString("reason_message") : "";
        String compositeKey = responseCode + "|" + reasonMessage;

        PanResponseToMerchant baseResponse = panVerifyResponseCache.getOrDefault(
                compositeKey,
                panVerifyResponseCache.getOrDefault(responseCode, panVerifyResponseCache.get("default")));

        //boolean success = "100".equals(responseCode);
        boolean success = "100".equalsIgnoreCase(responseCode);
		String ReasonMassage;
		if(!success)
		{
			ReasonMassage=reasonMessage;
		}
		else
		{
			ReasonMassage = input.optString("response_message");
		}
        PanResponseToMerchant.PanResponseToMerchantBuilder builder = baseResponse.toBuilder()
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
                .success(baseResponse.getSuccess());


        // If successful, populate full response details
        if (success) {
            builder.reference_id(requestId);

            if (metadata != null) {
                String billable = metadata.optString("billable");
                builder.billable(billable);
                if ("Y".equalsIgnoreCase(billable)) {
                    log.info("balance update Pan Provider Zoop Rate " + productRate);
                    updateBalance.updateWalletBalance(merchantId, productRate);
                }
            }

            JSONObject result = input.optJSONObject("result");
            if (result != null) {
                builder.pan_number(result.optString("pan_number"))
                        .user_full_name(result.optString("user_full_name"))
                        .user_email(result.optString("user_email"))
                        .user_phone_number(result.optString("user_phone_number"))
                        .user_gender(result.optString("user_gender"))
                        .user_dob(result.optString("user_dob"))
                        .masked_aadhaar(result.optString("masked_aadhaar"))
                        .aadhaar_linked_status(result.optBoolean("aadhaar_linked_status", false));
                JSONObject addressJson = result.optJSONObject("user_address");
                if (addressJson != null) {
                    PanResponseToMerchant.UserAddress userAddress = new PanResponseToMerchant.UserAddress();
                    userAddress.setCountry(addressJson.optString("country"));
                    userAddress.setState(addressJson.optString("state"));
                    userAddress.setCity(addressJson.optString("city"));
                    userAddress.setPincode(addressJson.optString("zip"));
                    userAddress.setStreet(addressJson.optString("street_name"));
                    userAddress.setFullAddress(addressJson.optString("full"));
                    builder.user_address(userAddress);
                }
                
            }
            //builder.order_id(orderId);
        } else {
            builder.billable(metadata != null ? metadata.optString("billable") : "N");
           // builder.order_id(orderId); 
            
        }
        PanResponseToMerchant response = builder.build();
        panResponseUpdate.updatePanResponse(trackId, response, input, response.getSuccess(), requestId, productRate,ReasonMassage);
        return new ResponseModel(success ? "success" : "failed", success ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(), response);
       
        
    }

	

	

  
}
