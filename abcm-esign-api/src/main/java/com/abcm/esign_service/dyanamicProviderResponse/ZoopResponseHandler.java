package com.abcm.esign_service.dyanamicProviderResponse;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.abcm.esign_service.DTO.ResponseModel;
import com.abcm.esign_service.DTO.VoterIdResponseToMerchant;
import com.abcm.esign_service.exception.CustomException;
import com.abcm.esign_service.repo.EsignRepository;
import com.abcm.esign_service.util.EsignResponseUpdate;
import com.abcm.esign_service.util.UrlHelper;
import com.abcmkyc.entity.KycData;

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
	private final EsignResponseUpdate esignResponseUpdate;
	private final EsignRepository repository;
	private final UrlHelper urlHelper;
	private final Map<String, VoterIdResponseToMerchant> voterIdVerifyResponseCache = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() {
		voterIdVerifyResponseCache.put("100", buildVoterIdVerifyResponse("voterId-validation-msg", true));
		voterIdVerifyResponseCache.put("104", buildVoterIdVerifyResponse("voterId-validation-failed-msg", false));

	}

	private VoterIdResponseToMerchant buildVoterIdVerifyResponse(String keyPrefix, boolean success) {

		log.info("prefix for error message : {}", keyPrefix);
		return VoterIdResponseToMerchant.builder().responseCode(environment.getProperty("custom.codes." + keyPrefix))
				.responseMessage(environment.getProperty("custom.messages." + keyPrefix)).success(success).build();
	}

	@Override
	public ResponseModel voterIdVerifyResponseToMerchant(JSONObject input, String trackId, String merchantId,
			Long productRate, String orderId) {
		try {
			log.info("Esign Zoop Original Response is: {}", input);
			boolean isSuccess = input.optBoolean("success");

			VoterIdResponseToMerchant.VoterIdResponseToMerchantBuilder responseBuilder = VoterIdResponseToMerchant
					.builder().merchantId(merchantId).orderId(orderId).success(isSuccess);

			List<KycData> kycList = repository.findByOrderId(orderId);

			Map<String, String> trackIdToRequestId = new ConcurrentHashMap<>();
			Map<String, String> trackIdToShortUrl = new ConcurrentHashMap<>();
			Map<String, String> trackIdToSigningUrl = new ConcurrentHashMap<>();

			List<VoterIdResponseToMerchant.SignerRequest> signerRequests = new ArrayList<>();

			JSONArray signersArray = input.optJSONArray("requests");
			if (signersArray != null && signersArray.length() > 0) {
				for (int i = 0; i < signersArray.length(); i++) {
					JSONObject s = signersArray.getJSONObject(i);
					String signerName = s.optString("signer_name");
					String requestId = s.optString("request_id");
					String signingUrl = s.optString("signing_url");
					String shortUrl = requestId != null ? urlHelper.generateLongUrl(requestId) : null;

					// Match KYC by customerName
					KycData matched = kycList.stream().filter(k -> signerName.equalsIgnoreCase(k.getCustomerName()))
							.findFirst().orElse(null);

					if (matched != null) {
						trackIdToRequestId.put(matched.getTrackId(), requestId);
						trackIdToShortUrl.put(matched.getTrackId(), shortUrl);
						trackIdToSigningUrl.put(matched.getTrackId(), signingUrl);

						VoterIdResponseToMerchant.SignerRequest signer = VoterIdResponseToMerchant.SignerRequest
								.builder().requestId(requestId).trackId(matched.getTrackId()).signerName(signerName)
								.signerEmail(matched.getSignerEmail()).signingUrl(shortUrl) // Can also use actual
																							// signingUrl if needed
								.build();

						signerRequests.add(signer);
					}
				}
			}

			if (isSuccess) {
				responseBuilder.responseCode("200");
				responseBuilder.responseMessage("Success");
			} else {
				responseBuilder.responseCode(input.optString("response_code", "400"));
				JSONObject metadata = input.optJSONObject("metadata");
				String reasonMessage = metadata != null ? metadata.optString("reason_message") : "Failed";
				responseBuilder.responseMessage(reasonMessage);
			}

			responseBuilder.signerRequests(signerRequests);
			VoterIdResponseToMerchant response = responseBuilder.build();

			log.info("Final Merchant Response: {}", response);

			esignResponseUpdate.updateVoterIdResponseBatch(kycList, response, input, isSuccess, trackIdToRequestId,
					productRate, trackIdToShortUrl, trackIdToSigningUrl);

			return new ResponseModel(isSuccess ? "success" : "failed",
					isSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(), response);

		} catch (Exception e) {
			log.error("Exception in mapping Zoop response", e);
			throw new CustomException(environment.getProperty("custom.messages.internal-server"),
					Integer.parseInt(environment.getProperty("custom.codes.internal-server")));
		}
	}

}
