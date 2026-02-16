package com.abcm.voterId.util;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.abcm.voterId.DTO.VoterIdResponseToMerchant;
import com.abcm.voterId.VoterIdRepo.VoterIdRepository;
import com.abcmkyc.entity.KycData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoterIdResponseUpdate {

	@Value("${email.failure.verify.voter.subject}")
	private String failuredlVerifySubject;

	@Value("${email.failure.template.path}")
	private String emailTemplatePath;

	

	@Value("${email.send.to}")
	private String SendTo;

	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_FAILED = "FAILED";
	private final VoterIdRepository repository;

	private final SendFailureEmail sendFailureEmail;

	public void updateVoterIdResponse(String trackId, VoterIdResponseToMerchant response, JSONObject input,
			boolean isSuccess, String requestId, Long productRate, String customerName, String reasonMsg) {
		try {
			KycData entity = repository.findByTrackId(trackId);
			if (entity == null) {
				log.warn("No entity found for trackId: {}", trackId);
				return;
			}
			String billable = "N";
			if (isSuccess) {
				billable = "Y";
			}
			// Update entity
			String VoterIdResponse = null;
            if (response != null) {
            	VoterIdResponse = new ObjectMapper().writeValueAsString(response);
            }
			KycData updatedEntity = entity.toBuilder().responseCode(response.getResponse_code())
					.responseMessage(response.getResponse_message()).requestId(requestId)
					.merchantResponseAt(java.time.LocalDateTime.now()).merchantResponse(VoterIdResponse)
					.ProviderResponse(input.toString()).status(isSuccess ? STATUS_SUCCESS : STATUS_FAILED)
					.billable(billable).productRate(productRate).customerName(customerName)
					.verificationId(input.optString("verification_id"))
					.reasonMessage(reasonMsg)
					.build();

			repository.save(updatedEntity);
			log.info("Updated response fields for trackId: {}, billable: {}", trackId, billable,"Reason Massage"+entity.getReasonMessage());
			if (!isSuccess) {
				log.info("Otp Send Failed Email Trigger From AadharResponseUpdate{}");
				String merchantId = nullToEmpty(entity.getMerchantId());
				String merchantName = nullToEmpty(entity.getMerchantName());
				String verificationType = nullToEmpty(entity.getProduct());
				String reasonMessage = nullToEmpty(entity.getReasonMessage());
				String requestTime = "";
				if (entity.getMerchantRequestAt() != null) {
					requestTime = entity.getMerchantRequestAt()
							.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
				}
				String mailString = mailString(merchantId, merchantName, trackId, verificationType, reasonMessage,
						requestTime);
				   String subject = failuredlVerifySubject
				    	    .replace("{productName}", verificationType);
				    	    //.replace("{reason}", reasonMessage);
				sendFailureEmail.sendEkycFailureEmail(mailString, trackId, subject, SendTo);
			}

		} catch (Exception e) {
			log.error("Error updating response in database for trackId: {}", trackId, e);
		}
	}

	/* Method to Handle Genrate Mail String to send Emial */
	public String mailString(String merchnat_id, String merchnatName, String trackId, String verification_type,
			String Reason, String requestTime) throws IOException {
		String mailstring1 = CommonUtils.readUsingFileInputStream(emailTemplatePath);
		mailstring1 = mailstring1.replace("{{MerchantID}}", merchnat_id);
		mailstring1 = mailstring1.replace("{{MerchantName}}", merchnatName);
		mailstring1 = mailstring1.replace("{{trackId}}", trackId);
		mailstring1 = mailstring1.replace("{{kycType}}", verification_type);
		mailstring1 = mailstring1.replace("{{errorDetails}}", Reason);
		mailstring1 = mailstring1.replace("{{requestTime}}", requestTime);
		return mailstring1;
	}

	private String nullToEmpty(String value) {
		return value != null ? value : "";
	}

}
