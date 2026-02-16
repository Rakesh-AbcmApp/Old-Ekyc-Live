package com.abcm.gst_service.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.abcm.gst_service.dto.MerchantToGstinResponse;
import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.util.GstResponseUpdate;
import com.abcm.gst_service.util.UpdateBalance;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component

public class CashfreeResponseHandler implements ProviderResponseHandler<ResponseModel> {

    @Autowired
    private Environment environment;

    @Autowired
    private GstResponseUpdate gstResponseUpdate;
    @Autowired
    UpdateBalance balance;

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);
    private static final String STATUS_VALID = "GSTIN Exists";

    private final Map<String, MerchantToGstinResponse> responseCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        responseCache.put("true", buildResponse("gst-valid-msg", true));
        responseCache.put("GSTIN_value_invalid", buildResponse("GST-format-validation", false));
        responseCache.put("GSTIN_missing", buildResponse("GST-format-validation", false));
        responseCache.put("insufficient_balance", buildResponse("porvider-blc-low", false));
        responseCache.put("too_many_requests_per_operation", buildResponse("porvider-blc-low", false));
        responseCache.put("null", buildResponse("provider-invalid-res", false));
        responseCache.put("default", buildResponse("gst-invalid-msg", false));
    }
    
    private MerchantToGstinResponse buildResponse(String keyPrefix, boolean success) {
        return MerchantToGstinResponse.builder()
                .response_code(environment.getProperty("custom.codes." + keyPrefix))
                .response_message(environment.getProperty("custom.messages." + keyPrefix))
                .success(success)
                .build();
    }

    private MerchantToGstinResponse getCachedResponse(Boolean valid, String type, String code, JSONObject responseObj) {
    	log.info("valid  status "+valid);
        if (valid) {
            return responseCache.getOrDefault("true", responseCache.get("default"));
        }
        if ("validation_error".equalsIgnoreCase(type)) {
            if ("GSTIN_value_invalid".equalsIgnoreCase(code)) {
                log.error("PAN format issue detected");
                return responseCache.getOrDefault("GSTIN_value_invalid", responseCache.get("default"));
            }
            if ("GSTIN_missing".equalsIgnoreCase(code)) {
                log.error("GSTIN_missing");
                return responseCache.getOrDefault("GSTIN_missing", responseCache.get("default"));
            }
            
            if ("insufficient_balance".equalsIgnoreCase(code)) {
                log.error("insufficient_balance");
                return responseCache.getOrDefault("insufficient_balance", responseCache.get("default"));
            }
        }
        
        if("rate_limit_error".equalsIgnoreCase(type))
        {
        	if ("GSTIN_value_invalid".equalsIgnoreCase(code)) {
                log.error("too_many_requests_per_operation");
                return responseCache.getOrDefault("too_many_requests_per_operation", responseCache.get("default"));
            }
        }
        return responseCache.get("default");
    }
    
    @Override
    public ResponseModel GstliteVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId, Long productRate,String orderId) {
        log.info("Received GSTIN Cashfree Response for MerchantId: {}", merchantId,orderId);
        String timestamp = ISO_FORMATTER.format(Instant.now());
        String refId = responseObj.optString("reference_id", "");
        Boolean valid = responseObj.optBoolean("valid");
        String message = responseObj.optString("GSTIN Exists");
        log.info("Cashfree MAssage-----------"+message);
        String type = responseObj.optString("type", "");
        String code = responseObj.optString("code", "");
       // String ReasonMassage = responseObj.optString("message", "");
        log.info("GST Response Details - Type: {}, Code: {}, Valid: {}", type, code, valid);
        boolean isValid = STATUS_VALID.equals(message);
		String ResonMessage;
		if(!isValid)
		{
			ResonMessage=  responseObj.optString("message");
		}else
		{
			ResonMessage=  responseObj.optString("message");
		}
        String billable = valid ? "Y" : "N";
        String responseStatus = valid ? "success" : "failed";
        int responseCode = valid ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
        MerchantToGstinResponse baseResponse = getCachedResponse(valid, type, code, responseObj);
        if (valid) {
        	log.info("Update balance success valid GSTIN orderId",orderId);
            balance.updateWalletBalance(merchantId, productRate);
        }
        MerchantToGstinResponse responseToMerchant = buildResponseToMerchant(
            valid, message, responseObj, baseResponse, refId, trackId, merchantId, billable, timestamp,orderId);
        gstResponseUpdate.updateGstResponse(trackId, responseToMerchant, responseObj, baseResponse.getSuccess(), refId, productRate,ResonMessage);
        log.info("Update success response gstin{}");
        return new ResponseModel(responseStatus, responseCode, responseToMerchant);
    }


    private MerchantToGstinResponse buildResponseToMerchant(
        boolean valid,
        String message,
        JSONObject responseObj,
        MerchantToGstinResponse baseResponse,
        String refId,
        String trackId,
        String merchantId,
        String billable,
        String timestamp, String orderId) {
        MerchantToGstinResponse.MerchantToGstinResponseBuilder builder;
        if (valid || ("GSTIN Exists".equalsIgnoreCase(message) && valid)) {
            builder = baseResponse.toBuilder()
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
                .referenceId(refId)
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .billable(billable)
                .gstin(responseObj.optString("GSTIN", ""))
                .legal_name(responseObj.optString("legal_name_of_business", ""))
                .trade_name(responseObj.optString("trade_name_of_business", ""))
                .register_date(responseObj.optString("date_of_registration", ""))
                .business_constitution(responseObj.optString("constitution_of_business", ""))
                .tax_payer_type(responseObj.optString("taxpayer_type", ""))
                .last_updated(responseObj.optString("last_update_date", ""))
                .state_jurisdiction(responseObj.optString("state_jurisdiction", ""))
                .central_jurisdiction(responseObj.optString("center_jurisdiction", ""));

            // Nature of business activities
            List<String> activities = new ArrayList<>();
            JSONArray activitiesArray = responseObj.optJSONArray("nature_of_business_activities");
            if (activitiesArray != null) {
                for (int i = 0; i < activitiesArray.length(); i++) {
                    activities.add(activitiesArray.optString(i));
                }
            }
            builder.nature_of_business_activities(activities);
            // Primary business address
            JSONObject primaryAddressObj = responseObj.optJSONObject("principal_place_split_address");
            if (primaryAddressObj != null) {
                MerchantToGstinResponse.primaryBusinessAddress primaryAddress = new MerchantToGstinResponse.primaryBusinessAddress();
                primaryAddress.setBuilding_name(primaryAddressObj.optString("building_name", ""));
                primaryAddress.setBuilding_number(primaryAddressObj.optString("building_number", ""));
                primaryAddress.setCity(primaryAddressObj.optString("city", ""));
                primaryAddress.setDistrict(primaryAddressObj.optString("district", ""));
                primaryAddress.setFlat_number(primaryAddressObj.optString("flat_number", ""));
                primaryAddress.setLatitude(primaryAddressObj.optString("latitude", ""));
                primaryAddress.setLocation(primaryAddressObj.optString("location", ""));
                primaryAddress.setLongitude(primaryAddressObj.optString("longitude", ""));
                primaryAddress.setBusiness_nature(primaryAddressObj.optString("business_nature", ""));
                primaryAddress.setPincode(primaryAddressObj.optString("pincode", ""));
                primaryAddress.setStreet(primaryAddressObj.optString("street", ""));
                primaryAddress.setState_code(primaryAddressObj.optString("state", ""));
                primaryAddress.setFull_address(responseObj.optString("principal_place_address", ""));
                builder.primary_business_address(primaryAddress);
            }
            List<MerchantToGstinResponse.OtherBusinessAddress> additionalAddresses = new ArrayList<>();
            JSONArray additionalAddressesArray = responseObj.optJSONArray("additional_address_array");
            if (additionalAddressesArray != null) {
                for (int i = 0; i < additionalAddressesArray.length(); i++) {
                    JSONObject obj = additionalAddressesArray.optJSONObject(i);
                    if (obj != null) {
                        JSONObject splitAddress = obj.optJSONObject("split_address");
                        MerchantToGstinResponse.OtherBusinessAddress address = new MerchantToGstinResponse.OtherBusinessAddress();
                        if (splitAddress != null) {
                            address.setBuilding_name(splitAddress.optString("building_name", ""));
                            address.setLocation(splitAddress.optString("location", ""));
                            address.setBuilding_number(splitAddress.optString("building_number", ""));
                            address.setDistrict(splitAddress.optString("district", ""));
                            address.setState(splitAddress.optString("state", ""));
                            address.setPincode(splitAddress.optString("pincode", ""));
                        }
                        address.setFull_address(obj.optString("address", ""));
                        additionalAddresses.add(address);
                    }
                }
            }
            builder.other_business_address(additionalAddresses);
            builder.order_id(orderId);

        } else {
            builder = baseResponse.toBuilder()
                .response_code(baseResponse.getResponse_code())
                .response_message(baseResponse.getResponse_message())
               // .referenceId(refId)
                .track_id(trackId)
                .merchant_id(merchantId)
                .response_timestamp(timestamp)
                .billable(billable)
                .order_id(orderId);
                
        }

        return builder.build();
    }

}
