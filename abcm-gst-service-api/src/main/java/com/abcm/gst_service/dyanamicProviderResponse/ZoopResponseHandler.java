package com.abcm.gst_service.dyanamicProviderResponse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.abcm.gst_service.dto.MerchantToGstinResponse;
import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.util.GstResponseUpdate;
import com.abcm.gst_service.util.UpdateBalance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private final GstResponseUpdate gstResponseUpdate;
	private final Map<String, MerchantToGstinResponse> GstliteVerifyResponseCache = new ConcurrentHashMap<>();

	public ZoopResponseHandler(Environment environment, UpdateBalance updateBalance,GstResponseUpdate gstResponseUpdate) {

		this.environment = environment;
		this.updateBalance = updateBalance;
		this.gstResponseUpdate=gstResponseUpdate;
	}

	@PostConstruct
	private void init() {
		// Initialize OTP Send response cache
		GstliteVerifyResponseCache.put("100", buildGstVerifyResponse("gst-valid-msg", true));
		GstliteVerifyResponseCache.put("106|Invalid Format for GSTIN", buildGstVerifyResponse("GST-format-validation", false));
		GstliteVerifyResponseCache.put("104", buildGstVerifyResponse("gst-invalid-msg", false));
		GstliteVerifyResponseCache.put("default", buildGstVerifyResponse("gst-invalid-msg", false));

	}
	private MerchantToGstinResponse buildGstVerifyResponse(String keyPrefix, boolean success) {
		return MerchantToGstinResponse.builder()
				.response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix))
				.success(success)
				.build();
	}

	@Override
	public ResponseModel GstliteVerifyResponseToMerchant(JSONObject input, String trackId, String merchantId, Long productRate,String orderId) {
	    log.info("Received GSTIN Zoop response: {}", merchantId);
	    String responseCode = input.optString("response_code");
	    JSONObject metadata = input.optJSONObject("metadata");
	    String requestId = input.optString("request_id");
	    String timestamp = ISO_FORMATTER.format(Instant.now());
	    String reasonMessage = metadata != null ? metadata.optString("reason_message") : "";
	    String compositeKey = responseCode + "|" + reasonMessage;
	    log.info("Gstin Failed Provider Reason"+reasonMessage);
	    MerchantToGstinResponse baseResponse = GstliteVerifyResponseCache.getOrDefault(
	        compositeKey,
	        GstliteVerifyResponseCache.getOrDefault(responseCode, GstliteVerifyResponseCache.get("default"))
	    );
	    String billable = metadata != null ? metadata.optString("billable") : "N";
	    String responseStatus = "100".equals(responseCode) ? "success" : "failed";
	    boolean isSuccess = "100".equalsIgnoreCase(responseCode);
	    int statusCode = "100".equals(responseCode) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value();
	   // boolean success = baseResponse.getSuccess();
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
	    log.info("Success"+success);
	    if ("Y".equalsIgnoreCase(billable)) {
	        log.info("Balance update for Pan Provider Zoop Rate: {}", merchantId);
	        updateBalance.updateWalletBalance(merchantId, productRate);
	    }
	    MerchantToGstinResponse responseToMerchant = buildResponseToMerchant(
	        success,
	        input,
	        baseResponse,
	        requestId,
	        trackId,
	        merchantId,
	        billable,
	        timestamp,responseCode,orderId
	    );
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
			String responseJson = objectMapper.writeValueAsString(responseToMerchant);
			log.info("JSON String"+responseJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    gstResponseUpdate.updateGstResponse(trackId, responseToMerchant, input, isSuccess, requestId, productRate,ReasonMassage);
	    return new ResponseModel(responseStatus, statusCode, responseToMerchant);
	}

	private MerchantToGstinResponse buildResponseToMerchant(
		    boolean success,
		    JSONObject input,
		    MerchantToGstinResponse baseResponse,
		    String requestId,
		    String trackId,
		    String merchantId,
		    String billable,
		    String timestamp,
		    String responseCode, String orderId) {
		    MerchantToGstinResponse.MerchantToGstinResponseBuilder builder;
		    
		    if ("100".equalsIgnoreCase(responseCode)) {
		        builder = baseResponse.toBuilder()
		            .track_id(trackId)
		            .merchant_id(merchantId)
		            .response_timestamp(timestamp)
		            .response_code(baseResponse.getResponse_code())
		            .response_message(baseResponse.getResponse_message())
		            .success(success)
		            .billable(billable);
		            

		        if (success) {
		            builder.referenceId(requestId);
		        }

		        JSONObject result = input.optJSONObject("result");
		        if (result != null) {
		            // Success case: null values replaced by ""
		            builder.gstin(nullToEmpty(result.optString("gstin")))
		                .legal_name(nullToEmpty(result.optString("legal_name")))
		                .trade_name(nullToEmpty(result.optString("trade_name")))
		                .register_date(nullToEmpty(result.optString("register_date")))
		                .business_constitution(nullToEmpty(result.optString("business_constitution")))
		                .tax_payer_type(nullToEmpty(result.optString("tax_payer_type")))
		                .last_updated(nullToEmpty(result.optString("last_updated")))
		                .state_jurisdiction(nullToEmpty(result.optString("state_jurisdiction")))
		                .central_jurisdiction(nullToEmpty(result.optString("central_jurisdiction")));

		            // business_nature array
		            if (result.has("business_nature")) {
		                var businessNatureArray = result.optJSONArray("business_nature");
		                if (businessNatureArray != null) {
		                    var natureList = new java.util.ArrayList<String>();
		                    for (int i = 0; i < businessNatureArray.length(); i++) {
		                        natureList.add(businessNatureArray.optString(i, ""));
		                    }
		                    builder.nature_of_business_activities(natureList);
		                }
		            }

		            // other_business_address array
		            if (result.has("other_business_address")) {
		                var otherAddrArray = result.optJSONArray("other_business_address");
		                if (otherAddrArray != null) {
		                    var otherAddresses = new java.util.ArrayList<MerchantToGstinResponse.OtherBusinessAddress>();
		                    for (int i = 0; i < otherAddrArray.length(); i++) {
		                        JSONObject addrJson = otherAddrArray.optJSONObject(i);
		                        if (addrJson != null) {
		                            MerchantToGstinResponse.OtherBusinessAddress otherAddr = new MerchantToGstinResponse.OtherBusinessAddress();
		                            otherAddr.setPincode(nullToEmpty(addrJson.optString("pincode")));
		                            otherAddr.setState(nullToEmpty(addrJson.optString("state")));
		                            otherAddr.setDistrict(nullToEmpty(addrJson.optString("district")));
		                            otherAddr.setLocation(nullToEmpty(addrJson.optString("location")));
		                            otherAddr.setBuilding_number(nullToEmpty(addrJson.optString("building_number")));
		                            otherAddr.setBuilding_name(nullToEmpty(addrJson.optString("building_name")));
		                            otherAddr.setFull_address(nullToEmpty(addrJson.optString("full_address")));
		                            otherAddresses.add(otherAddr);
		                        }
		                    }
		                    builder.other_business_address(otherAddresses);
		                }
		            }

		            // primary_business_address
		            JSONObject primaryAddrJson = result.optJSONObject("primary_business_address");
		            if (primaryAddrJson != null) {
		                MerchantToGstinResponse.primaryBusinessAddress primaryAddr = new MerchantToGstinResponse.primaryBusinessAddress();
		                primaryAddr.setBuilding_name(nullToEmpty(primaryAddrJson.optString("building_name")));
		                primaryAddr.setBuilding_number(nullToEmpty(primaryAddrJson.optString("building_number")));
		                primaryAddr.setCity(nullToEmpty(primaryAddrJson.optString("city")));
		                primaryAddr.setDistrict(nullToEmpty(primaryAddrJson.optString("district")));
		                primaryAddr.setFlat_number(nullToEmpty(primaryAddrJson.optString("flat_number")));
		                primaryAddr.setLatitude(nullToEmpty(primaryAddrJson.optString("latitude")));
		                primaryAddr.setLocation(nullToEmpty(primaryAddrJson.optString("location")));
		                primaryAddr.setLongitude(nullToEmpty(primaryAddrJson.optString("longitude")));
		                primaryAddr.setBusiness_nature(nullToEmpty(primaryAddrJson.optString("business_nature")));
		                primaryAddr.setPincode(nullToEmpty(primaryAddrJson.optString("pincode")));
		                primaryAddr.setStreet(nullToEmpty(primaryAddrJson.optString("street")));
		                primaryAddr.setState_code(nullToEmpty(primaryAddrJson.optString("state_code")));
		                primaryAddr.setFull_address(nullToEmpty(primaryAddrJson.optString("full_address")));
		                builder.primary_business_address(primaryAddr);
		            }
		        }
                builder.order_id(orderId);
		        return builder.build();
		    } else {
		        // Failed case: set only non-null and non-empty fields
		        builder = baseResponse.toBuilder()
		            .response_code(baseResponse.getResponse_code())
		            .response_message(baseResponse.getResponse_message())
		            .track_id(trackId)
		            .merchant_id(merchantId)
		            .response_timestamp(timestamp)
		            .billable(billable)
		            .order_id(orderId);
		        return builder.build();
		    }
		}

		
		private String nullToEmpty(String value) {
		    return value == null ? "" : value;
		}








}
