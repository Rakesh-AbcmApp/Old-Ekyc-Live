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
import com.abcm.voterId.exception.CustomException;
import com.abcm.voterId.util.UpdateBalance;
import com.abcm.voterId.util.VoterIdResponseUpdate;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ZoopResponseHandler implements ProviderResponseHandler<ResponseModel> {

	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneOffset.UTC);

	private final Environment environment;
	private final UpdateBalance updateBalance;
	private final VoterIdResponseUpdate voterIdResponseUpdate;
	private final Map<String, VoterIdResponseToMerchant> voterIdVerifyResponseCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		voterIdVerifyResponseCache.put("100", buildVoterIdVerifyResponse("voterId-validation-msg", true));
		voterIdVerifyResponseCache.put("104", buildVoterIdVerifyResponse("voterId-validation-failed-msg", false));
		voterIdVerifyResponseCache.put("106|Invalid format for Voter EPIC number ",
				buildVoterIdVerifyResponse("voterId-format-validation", false));
		voterIdVerifyResponseCache.put("106", buildVoterIdVerifyResponse("voterId-format-validation", false));
		voterIdVerifyResponseCache.put("112", buildVoterIdVerifyResponse("voterId-invalid-res", false));
		voterIdVerifyResponseCache.put("113", buildVoterIdVerifyResponse("provider-invalid-res", false));
		voterIdVerifyResponseCache.put("default", buildVoterIdVerifyResponse("voterId-validation-failed-msg", false));

	}

	private VoterIdResponseToMerchant buildVoterIdVerifyResponse(String keyPrefix, boolean success) {

		log.info("prefix for error message : {}", keyPrefix);
		return VoterIdResponseToMerchant.builder().response_code(environment.getProperty("custom.codes." + keyPrefix))
				.response_message(environment.getProperty("custom.messages." + keyPrefix)).success(success).build();
	}

	@Override
	public ResponseModel voterIdVerifyResponseToMerchant(JSONObject input, String trackId, String merchantId,
			Long productRate,String orderId) {
		
		try {
			log.info("zoop hanlder voter id",merchantId,orderId);
			String responseCode = input.optString("response_code");
			JSONObject metadata = input.optJSONObject("metadata");
			String requestId = input.optString("request_id");
			String timestamp = ISO_FORMATTER.format(Instant.now());
			String reasonMessage = metadata != null ? metadata.optString("reason_message") : "";
			String compositeKey = responseCode + "|" + reasonMessage;
			boolean isSuccess = "100".equalsIgnoreCase(responseCode);
			String ReasonMassage;
			if(!isSuccess)
			{
				ReasonMassage=reasonMessage;
			}
			else
			{
				ReasonMassage = input.optString("response_message");
			}

			log.info("composite key is  : {} ", compositeKey);
			VoterIdResponseToMerchant baseResponse = voterIdVerifyResponseCache.getOrDefault(compositeKey,
					voterIdVerifyResponseCache.getOrDefault(responseCode, voterIdVerifyResponseCache.get("default")));

			boolean success = "100".equals(responseCode);

			VoterIdResponseToMerchant.VoterIdResponseToMerchantBuilder builder = baseResponse.toBuilder()
					.track_id(trackId)
					.merchant_id(merchantId)
					.responseTimestamp(timestamp)
					.response_code(baseResponse.getResponse_code())
					.referenceId(requestId)
					.response_message(baseResponse.getResponse_message())
					.success(success);
					//.order_id(orderId);

			if (metadata != null) {
				String billable = metadata.optString("billable");
				builder.billable(billable);
				if ("Y".equalsIgnoreCase(billable) && success) {
					log.info("Balance update: Voter ID Provider Zoop Rate {}", productRate);
					updateBalance.updateWalletBalance(merchantId, productRate);
				}
			}

			JSONObject result = input.optJSONObject("result");
			String customerName="";
			if (success && result != null) {
				 customerName=result.optString("user_name_english");
				log.info("recived sucess from ZOOP");
				builder.epicNumber(result.optString("epic_number")).status("VALID")
						.name(result.optString("user_name_english"))
						.gender(normalizeGender(result.optString("user_gender"))).age(result.optInt("user_age"))
						.relationType(result.optString("relative_relation"))
						.relationName(result.optString("relative_name_english"))
						.parliamentaryConstituencyNumber(result.optInt("parliamentary_constituency_number"))
						.parliamentaryConstituency(result.optString("parliamentary_constituency_name"))
						.assemblyConstituencyNumber(result.optInt("assembly_constituency_number"))
						.assemblyConstituency(result.optString("assembly_constituency_name"))
						.partNumber(String.valueOf(result.optInt("constituency_part_number")))
						.partName(result.optString("constituency_part_name"))
						.serialNumber(String.valueOf(result.optInt("serial_number_applicable_part")));

				JSONObject pollingBooth = result.optJSONObject("polling_booth");
				if (pollingBooth != null) {
					builder.pollingStationName(pollingBooth.optString("name"));
				}

				builder.requestTimestamp(input.optString("request_timestamp"))
						.responseTimestamp(input.optString("response_timestamp"));

			}

			VoterIdResponseToMerchant response = builder.build();

			response.setAge(response.getAge() != null && response.getAge() == 0 ? null : response.getAge());
			response.setParliamentaryConstituencyNumber(response.getParliamentaryConstituencyNumber() != null
					&& response.getParliamentaryConstituencyNumber() == 0 ? null
							: response.getParliamentaryConstituencyNumber());
			response.setAssemblyConstituencyNumber(
					response.getAssemblyConstituencyNumber() != null && response.getAssemblyConstituencyNumber() == 0 ? null
							: response.getAssemblyConstituencyNumber());
			
			log.info(" voter id zoop set merchant to Response{}",response);
			voterIdResponseUpdate.updateVoterIdResponse(trackId, response, input, response.getSuccess(), requestId,
					productRate,customerName,ReasonMassage);

			
			return new ResponseModel(isSuccess ? "success" : "failed",
					isSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(), response);
			
		} catch (Exception e) {
			log.error("voter id response to merchant exception{}",e.getMessage());
			
			throw new CustomException(environment.getProperty("custom.messages.internal-server"),
					Integer.parseInt(environment.getProperty("custom.codes. internal-server")));
		}
		
	}

	private String normalizeGender(String gender) {
	    if (gender == null) {
	        return null;
	    }

	    return switch (gender.trim().toUpperCase()) {
	        case "M" -> "Male";
	        case "F" -> "Female";
	        case "O" -> "Other";
	        default -> gender;
	    };
	}

}
