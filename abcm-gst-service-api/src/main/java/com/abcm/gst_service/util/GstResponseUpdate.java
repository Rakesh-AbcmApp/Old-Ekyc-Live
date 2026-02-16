package com.abcm.gst_service.util;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.abcm.gst_service.dto.MerchantToGstinResponse;
import com.abcm.gst_service.exception.CustomException;
import com.abcm.gst_service.repository.GstVerificationRepository;
import com.abcmkyc.entity.KycData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class GstResponseUpdate {
	@Value("${email.failure.verify.gstin.subject}")
	private String failuredlVerifySubject;
	
	
	@Value("${email.failure.template.path}")
	private String emailTemplatePath;
	
	
	 @Value("${email.send.to}")
	    private String SendTo;
	 
	 

	
	 private final SendFailureEmail sendFailureEmail;
	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_FAILED = "FAILED";    
	private final GstVerificationRepository gstVerificationRepository;
	private final Environment environment;
	public GstResponseUpdate(Environment environment,GstVerificationRepository gstVerificationRepository,SendFailureEmail sendFailureEmail) {
		this.gstVerificationRepository = gstVerificationRepository;
		this.environment=environment;
		this.sendFailureEmail=sendFailureEmail;
	}
	public void updateGstResponse(String trackId, MerchantToGstinResponse response, JSONObject input, boolean isSuccess, String requestId,Long productRate,String Reason) {
		try {
			log.info("Gst Update Response Agents TrackId{}",trackId,"---------",isSuccess);
			KycData entity = gstVerificationRepository.findByTrackId(trackId);
			//log.info("Gst Update Response Agents Mid,TrackId{}",trackId);
			if (entity == null) {
				log.warn("No entity found for trackId: {}");
				return;
			}
			log.info("Gst Update Response Agents MerchantId{}",entity.getMerchantId());
			String billable = "N";
			if (isSuccess) {
				billable = "Y";
			}
			// Update entity
			String gstResponse=null;
			if(response!=null)
			{
				gstResponse = new ObjectMapper().writeValueAsString(response);
	
			}
			//log.info("JSOn String"+gstResponse);
			KycData updatedEntity = entity.toBuilder()
					.responseCode(response.getResponse_code())
					.responseMessage(response.getResponse_message())
					.requestId(requestId)
					.merchantResponseAt(java.time.LocalDateTime.now())
					.merchantResponse(gstResponse)
					.ProviderResponse(input.toString())
					.status(isSuccess ? STATUS_SUCCESS : STATUS_FAILED)
					.billable(billable)
					.productRate(productRate)
					.legalName(response.getLegal_name())
					.reasonMessage(Reason)
					.build();
			gstVerificationRepository.save(updatedEntity);
			log.info("Updated response fields for trackId: {}, billable: {}", trackId, billable,Reason);
			
			 if (!isSuccess) {
				    log.info("Send Mail Trigger To DL Verification failed");
				    String merchantId = nullToEmpty(entity.getMerchantId());
				    String merchantName = nullToEmpty(entity.getMerchantName());
				    String verificationType = nullToEmpty(entity.getProduct());
				    String reasonMessage = nullToEmpty(entity.getReasonMessage());
				    String requestTime = "";
				    if (entity.getMerchantRequestAt() != null) {
				        requestTime = entity.getMerchantRequestAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
				    }
				    String mailString = mailString(merchantId, merchantName, trackId, verificationType, reasonMessage, requestTime);
				    String subject = failuredlVerifySubject
				    	    .replace("{productName}", verificationType);
				    	   // .replace("{reason}", reasonMessage);

				    //sendFailureEmail.sendEkycFailureEmail(mailString, trackId, subject,SendTo);
				}

		} catch (Exception e) {
			new CustomException(environment.getProperty("custom.messages.internal-server"),
					Integer.parseInt(environment.getProperty("custom.codes.internal-server")));
		}
		
		
		
	}
	
	
	  /*Method to Handle Genrate Mail String to send Emial*/
			public String mailString (String merchnat_id,String  merchnatName,  String trackId, String verification_type,  String Reason,
					String requestTime) throws IOException
			{
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
