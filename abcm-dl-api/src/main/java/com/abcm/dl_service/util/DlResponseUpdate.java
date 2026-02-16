package com.abcm.dl_service.util;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.abcm.dl_service.dto.DlResponseToMerchant;
import com.abcm.dl_service.repository.DlVerificationRepository;
import com.abcmkyc.entity.KycData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class DlResponseUpdate {
	
	@Value("${email.failure.verify.dl.subject}")
	private String failuredlVerifySubject;
	
	
	@Value("${email.failure.template.path}")
	private String emailTemplatePath;
	
	
	 @Value("${email.send.to}")
	    private String to;
	 
	

	
	
	
	    private static final String STATUS_SUCCESS = "SUCCESS";
	    private static final String STATUS_FAILED = "FAILED";    
	    private final DlVerificationRepository dlVerificationRepository;
	    private final SendFailureEmail sendFailureEmail;
	    public DlResponseUpdate(Environment environment,DlVerificationRepository dlVerificationRepository,SendFailureEmail sendFailureEmail) {
	       
	        this.dlVerificationRepository = dlVerificationRepository;
	        this.sendFailureEmail=sendFailureEmail;
	    }
	    public void updateDlResponse(String trackId, DlResponseToMerchant response, JSONObject input, boolean isSuccess, String requestId,Long productRate,String reason) {
	        try {
	        	log.info("update dl response db inside{}");
	            KycData entity = dlVerificationRepository.findByTrackId(trackId);
	            if (entity == null) {
	                log.warn("No entity found for trackId: {}", trackId);
	                return;
	            }
	            String billable = "N";
	            if (isSuccess) {
	                billable = "Y";
	            }
	            // Update entity
	            String dlResponse = null;
	            if (response != null) {
	                dlResponse = new ObjectMapper().writeValueAsString(response);
	            }
	            
	            KycData updatedEntity = entity.toBuilder()
	                    .responseCode(response.getResponse_code())
	                    .responseMessage(response.getResponse_message())
	                    .requestId(requestId)
	                    .merchantResponseAt(java.time.LocalDateTime.now())
	                    .merchantResponse(dlResponse)
	                    .ProviderResponse(input.toString())
	                    .status(isSuccess ? STATUS_SUCCESS : STATUS_FAILED)
	                    .billable(billable)
	                    .productRate(productRate)
	                    .customerName(response.getName())
	                    .verificationId(input.optString("verification_id"))
	                    .reasonMessage(reason)
	                    .build();
	            dlVerificationRepository.save(updatedEntity);
	            log.info("Updated response fields for trackId: {}, billable: {}", trackId, billable);
	            if (!isSuccess) {
				    log.info("Otp Send Failed Email Trigger From AadharResponseUpdate{}");
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
				    	    
				    sendFailureEmail.sendEkycFailureEmail(mailString, trackId, subject,to);
				}
	        } catch (Exception e) {
	            log.error("Error updating response in database for trackId: {}", trackId, e);
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
