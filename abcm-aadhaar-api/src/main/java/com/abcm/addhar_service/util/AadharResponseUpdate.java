package com.abcm.addhar_service.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abcm.addhar_service.Email.CommonUtils;
import com.abcm.addhar_service.Email.SendFailureEmail;
import com.abcm.addhar_service.merchantReponseDto.DigiReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.DigiWebhookReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpSendReponseToMerchnat;
import com.abcm.addhar_service.merchantReponseDto.OtpVerifyResponseToMerchant;
import com.abcm.addhar_service.repository.AadharRepository;
import com.abcmkyc.entity.KycData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AadharResponseUpdate {

	
	@Value("${email.failure.send.otp.subject.prefix}")
	private String sendOtpSubject;

	@Value("${email.failure.verify.otp.subject.prefix}")
	private String verifyOtpSubject;
	
	
	@Value("${email.failure.verify.dglocker.subject.prefix}")
	private String dgfailedSubject;
	
	
	@Value("${email.failure.template.path}")
	private String emailTemplatePath;
	
	 @Value("${email.send.to}")
	    private String to;
	 
	 
	 

	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_FAILED = "FAILED";

	private final AadharRepository aadharRepository;
	private final SendFailureEmail sendFailureEmail;

	public AadharResponseUpdate(Environment environment, AadharRepository aadharRepository,
			SendFailureEmail sendFailureEmail) {

		this.aadharRepository = aadharRepository;
		this.sendFailureEmail = sendFailureEmail;
	}

	@Async
	public void updateOtpSendResponse(String trackId, OtpSendReponseToMerchnat response, JSONObject input,
	        boolean isSuccess, String request_id, String reasonMassage) {
		log.info("updateOtpSendResponse in DB method ",trackId);
	    String ref_id = "";
	    if (input != null && input.has("status") && !input.isNull("status")) {
	        JSONObject jsonObjectstatus = input.optJSONObject("status");
	        if (jsonObjectstatus != null) {
	            ref_id = jsonObjectstatus.optString("ref_id", "");
	        }
	    }
	    try {
	        KycData entity = aadharRepository.findByTrackId(trackId);
	        if (entity == null) {
	            log.warn("No entity found for trackId: {}", trackId);
	            return;
	        }
	        log.info("Update billabe in databsed otp send Response"+response.getBillable());
	        KycData updatedEntity = entity.toBuilder()
	                .responseCode(response.getResponse_code())
	                .responseMessage(response.getResponse_message())
	                .requestId(request_id)
	                .verificationId(ref_id)
	                .merchantRequestAt(java.time.LocalDateTime.now())
	                .merchantResponse(new ObjectMapper().writeValueAsString(response))
	                .ProviderResponse(new ObjectMapper().writeValueAsString(input))
	                .status(isSuccess ? STATUS_SUCCESS : STATUS_FAILED)
	                .billable("N")
	                .reasonMessage(reasonMassage)
	                // .identificationNo(trackId)
	                .build();
	        aadharRepository.save(updatedEntity);
	        log.info("Updated response fields for trackId: {}", trackId);
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
	            String subject = sendOtpSubject.replace("{productName}", verificationType);
	            sendFailureEmail.sendAadhaarFailureEmail(mailString, trackId, subject, to);
	        }

	    } catch (Exception e) {
	        log.error("Error updating response in database for trackId: {}", trackId, e);
	    }
	}

	
	
	
	
	@Async
    public void updateDigiResponse(String trackId, DigiReponseToMerchnat responseToMerchant, JSONObject input,
            boolean responseSuccess, String requestId, String webhookSecurityKey,String reasonMessage,String sdkUrl,String shortUrl ) {
         try {
        	 
        	       log.info("updateDigiResponse in DB  method ",trackId);
        	 
                   KycData entity = aadharRepository.findByTrackId(trackId);
                if (entity == null) {
                    log.warn("No entity found for trackId: {}", trackId);
                    return;
                }

                // Update only  required fields, use entity.toBuilder for immutability but minimal changes
                KycData updatedEntity = entity.toBuilder()
                        .responseCode(responseToMerchant.getResponse_code())
                        .responseMessage(responseToMerchant.getResponse_message())
                        .webhookSecurityKey(webhookSecurityKey) 
                        .requestId(requestId)
                        .merchantRequestAt(java.time.LocalDateTime.now())
                        .merchantResponse(new ObjectMapper().writeValueAsString(responseToMerchant))
                        .ProviderResponse(new ObjectMapper().writeValueAsString(input))
                        .status(responseSuccess ? STATUS_SUCCESS : STATUS_FAILED)
                        .billable("N")
                        .reasonMessage(reasonMessage)
                        .sdkUrl(sdkUrl)
                        .shortUrl(shortUrl)
                        .build();
                aadharRepository.save(updatedEntity);
                log.info("Updated response fields for trackId: {}", trackId);
                if (!responseSuccess) {
    				log.info("Otp Send Failed Emial Trigger From  AadharResponseUpdate{}:");
    				String merchnat_id = entity.getMerchantId();
    				String merchnatName = entity.getMerchantName();
    				String verification_type = entity.getProduct();
    				String Reason = entity.getReasonMessage();
    				
    				LocalDateTime requestAt = entity.getMerchantRequestAt();
    				String requestTime = requestAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    				String MailString= mailString (merchnat_id, merchnatName, trackId, verification_type, Reason,
    						requestTime);
    				String subject = dgfailedSubject
				    	    .replace("{productName}", verification_type)
				    	    .replace("{reason}", reasonMessage);
    				sendFailureEmail.sendAadhaarFailureEmail(MailString,trackId, subject,to);
    			}


            } catch (Exception e) {
                log.error("Error updating response in database for trackId: {}", trackId, e);
            }
    }

	@Async
    public KycData updateWebhookResponse(String billable, String status, String requestId, String webhookSecurityKey,
            KycData entity,String payload,String name,DigiWebhookReponseToMerchnat response,String responseMessage) {
             try {
               log.info("inside updateWebhookResponse DB  ::",entity.getTrackId());
                // Update only required fields, use entity.toBuilder for immutability but minimal changes
                KycData updatedEntity = entity.toBuilder()
//                        .responseCode(responseToMerchant.getResponse_code())
//                        .responseMessage(responseToMerchant.getResponse_message())
                        .webhookSecurityKey(webhookSecurityKey) 
                        .requestId(requestId)
                        .merchantRequestAt(java.time.LocalDateTime.now())
                        .merchantResponse(new ObjectMapper().writeValueAsString(response))
                        .ProviderResponse(new ObjectMapper().writeValueAsString(payload))
                        .customerName(name)
//                        .status(responseSuccess ? STATUS_SUCCESS : STATUS_FAILED)
                        .billable(billable)
                       // .identificationNo(trackId)
                        .otpVerify(true)
                        .merchantResponseAt(java.time.LocalDateTime.now())
                        .responseMessage("Aadhaar Verified Successfully")
                        .webhookStatus(true)
                        .reasonMessage(responseMessage)
                        .build();
                return aadharRepository.save(updatedEntity);
            //    log.info("Updated response fields for webhookSecurityKey: {}");

            } catch (Exception e) {
                log.error("Error updating response in database for webhookSecurityKey: {}", webhookSecurityKey, e.getMessage());
            return null;
            }
    }

	@Async
	public void updateOtpVerifyResponse( OtpVerifyResponseToMerchant response, JSONObject input,
			boolean isSuccess, String requestId, Long productRate, String reason,KycData kycData) {
		try {
			
			
			String ref_id = "";
		    if (input != null && input.has("status") && !input.isNull("status")) {
		        JSONObject jsonObjectstatus = input.optJSONObject("status");
		        if (jsonObjectstatus != null) {
		            ref_id = jsonObjectstatus.optString("ref_id", "");
		        }
		    }
			log.info("merchantResponse{}====="+new ObjectMapper().writeValueAsString(response));			
			    String respCode = response != null ? response.getResponse_code() : null;
		        String respMsg  = response != null ? response.getResponse_message() : null;
		        String fullName = response != null ? response.getUserFullName() : null;
		        
		        
			    
			// Build updated entity
			KycData updatedkycData = kycData.toBuilder()
					.responseCode(respCode)
					.responseMessage(respMsg)
					//.requestId(requestId)
					.verificationId(ref_id)
					.merchantResponseAt(java.time.LocalDateTime.now())
					.adharVerifyresponse(new ObjectMapper().writeValueAsString(response))
					.providerVerifyresponse(new ObjectMapper().writeValueAsString(input))
					.status(isSuccess ? STATUS_SUCCESS : STATUS_FAILED)
					.billable(isSuccess ? "Y" : "N")
					.otpVerify(isSuccess)
					.productRate(productRate)
					.identificationNo(kycData.getIdentificationNo())
					.customerName(fullName)
					.reasonMessage(reason).build();
			        aadharRepository.save(updatedkycData);
			log.info("Updated response fields for trackId: {}", kycData.getTrackId());
			if (!isSuccess) {
			    log.info("Otp Send Failed Email Trigger From AadharResponseUpdate{}");
			    String merchantId = nullToEmpty(kycData.getMerchantId());
			    String merchantName = nullToEmpty(kycData.getMerchantName());
			    String verificationType = nullToEmpty(kycData.getProduct());
			    String reasonMessage = nullToEmpty(kycData.getReasonMessage());
			    String requestTime = "";
			    if (kycData.getMerchantRequestAt() != null) {
			        requestTime = kycData.getMerchantRequestAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			    }
			    String mailString = mailString(merchantId, merchantName, kycData.getTrackId(), verificationType, reasonMessage, requestTime);
			    String subject = verifyOtpSubject
			    	    .replace("{productName}", verificationType);
			    	    //.replace("{reason}", reasonMessage);
			    sendFailureEmail.sendAadhaarFailureEmail(mailString, kycData.getTrackId(), subject,to);
			}

		} catch (Exception e) {
			log.error("Error updating response in database for trackId: {}", kycData.getTrackId(), e);
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
        mailstring1 = mailstring1.replace("{{errorDetails}}", nullToEmpty(Reason));
        mailstring1 = mailstring1.replace("{{requestTime}}", requestTime);
        return mailstring1;
	}
	
	 
	private String nullToEmpty(String value) {
	    return value != null ? value : "";
	}
	
	

}
