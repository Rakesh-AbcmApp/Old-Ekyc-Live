package com.abcm.dl_service.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class ZoopResponseHandler implements ProviderResponseHandler<ResponseModel> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);

    private final Environment environment;
    private final UpdateBalance updateBalance;
    private final DlResponseUpdate dlResponseUpdate;
    
    
    
    private final Map<String, DlResponseToMerchant> panVerifyResponseCache = new ConcurrentHashMap<>();
    public ZoopResponseHandler(Environment environment, UpdateBalance updateBalance,DlResponseUpdate dlResponseUpdate) {
      
        this.environment = environment;
        this.updateBalance = updateBalance;
        this.dlResponseUpdate=dlResponseUpdate;
    }

    
    @PostConstruct
    private void init() {
        panVerifyResponseCache.put("100", buildDlVerifyResponse("dl-validation-msg", true));
        panVerifyResponseCache.put("101", buildDlVerifyResponse("dl-recort-not-found", true));
        panVerifyResponseCache.put("104", buildDlVerifyResponse("dl-validation-failed-msg", false));
        panVerifyResponseCache.put("108", buildDlVerifyResponse("dl-validation-failed-msg", false));
        panVerifyResponseCache.put("106|Customer DOB should of format dd-mm-yyyy", buildDlVerifyResponse("dl-dob-format-validation", false));
        panVerifyResponseCache.put("106|DL Number should be minimum 14 characters", buildDlVerifyResponse("dl-invalid-no", false));
        panVerifyResponseCache.put("106", buildDlVerifyResponse("dl-validation-failed-msg", false));
        panVerifyResponseCache.put("112", buildDlVerifyResponse("dl-validation-failed-msg", false));
        panVerifyResponseCache.put("113", buildDlVerifyResponse("dl-validation-failed-msg", false));
        panVerifyResponseCache.put("default", buildDlVerifyResponse("dl-validation-failed-msg", false));
    }
    private DlResponseToMerchant buildDlVerifyResponse(String keyPrefix, boolean success) {
    	log.info("Keyc"+keyPrefix);
    	DlResponseToMerchant response = DlResponseToMerchant.builder()
                .response_code(environment.getProperty("custom.codes." + keyPrefix))
                .response_message(environment.getProperty("custom.messages." + keyPrefix))
                .success(success)
                .build();

        //log.info("✅ Built DL Response for keyPrefix '{}': {}", keyPrefix, response);
        return response;
    }

 
    public ResponseModel DlVerifyResponseToMerchant(JSONObject input, String trackId, String merchantId, Long productRate,String orderId) {
        String responseCode = input.optString("response_code");
        String responseMassage = input.optString("response_message");
        JSONObject metadata = input.optJSONObject("metadata");
        String requestId = input.optString("request_id");
        String timestamp = ISO_FORMATTER.format(Instant.now());
        String reasonMessage = metadata != null ? metadata.optString("reason_message") : "";
        String ReasonMassage = null;
        String compositeKey = responseCode + "|" + reasonMessage;
        boolean success = "100".equals(responseCode);
        if(!success)
        {
        	if(reasonMessage.isEmpty())
        	{
        		ReasonMassage=responseMassage;
        		
        	}
        	else
        	{
        		ReasonMassage=reasonMessage;
        		
        	}
        }
        else
        {
        	ReasonMassage=responseMassage;
        	
        }
        
        DlResponseToMerchant baseResponse = panVerifyResponseCache.getOrDefault(
                compositeKey,
                panVerifyResponseCache.getOrDefault(responseCode, panVerifyResponseCache.get("default")));
       
        DlResponseToMerchant.DlResponseToMerchantBuilder builder = baseResponse.toBuilder()
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
                .success(baseResponse.getSuccess());

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
                builder.dl_number(result.optString("dl_number"))
                        .dl_validity(result.optString("expiry_date"))
                        .date_of_issue(result.optString("date_of_issue"))
                        .name(result.optString("user_full_name"))
                        .father_or_husband_name(result.optString("user_gender"))
                        .user_image(result.optString("user_image"));
                JSONArray userAddressArray = result.optJSONArray("user_address");
                if (userAddressArray != null && userAddressArray.length() > 0) {
                    JSONObject address = userAddressArray.optJSONObject(0);
                    if (address != null) {
                        builder.complete_address(address.optString("completeAddress"))
                               .district(address.optString("district"))
                               .state(address.optString("state"))
                               .city(address.optString("city"))  // If city isn't provided separately
                               .pincode(address.optString("pin",""))  // If city isn't provided separately
                               .country(address.optString("country"));
                    }
                    builder.cov_details(
							ToSafeStringMapList.covSafeStringMapList(result.optJSONArray("vehicle_category_details")));
                }
                String status = "100".equals(responseCode) ? "VALID" : "INVALID"; // ✅ Corrected
                log.info("Code with Status: " + responseCode + " => " + status);
                builder.status(status);
                //.order_id(orderId);
            }
        } else {
        	//log.info("--------------------"+baseResponse.getResponse_message()+"00000000000000000"+baseResponse.getResponse_code());
        	String billable = metadata != null ? metadata.optString("billable", "N") : "N";
            builder
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
                .billable(billable)
                .success(false)
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp);
               // .order_id(orderId);
                //.reference_id(requestId);
            
        }
        DlResponseToMerchant response = builder.build();
        dlResponseUpdate.updateDlResponse(trackId, response, input, response.getSuccess(), requestId, productRate,ReasonMassage);
        boolean isSuccess = "100".equalsIgnoreCase(responseCode);
        return new ResponseModel(isSuccess ? "success" : "failed", isSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(), response);
       
        
    }

	

	

  
}
