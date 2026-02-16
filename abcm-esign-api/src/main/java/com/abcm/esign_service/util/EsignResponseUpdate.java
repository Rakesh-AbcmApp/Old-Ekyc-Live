package com.abcm.esign_service.util;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.abcm.esign_service.DTO.VoterIdResponseToMerchant;
import com.abcm.esign_service.repo.EsignRepository;
import com.abcmkyc.entity.KycData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EsignResponseUpdate {

    @Value("${email.failure.verify.voter.subject}")
    private String failuredlVerifySubject;

    @Value("${email.failure.template.path}")
    private String emailTemplatePath;

    @Value("${email.send.to}")
    private String SendTo;

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";

    private final EsignRepository repository;
    private final SendFailureEmail sendFailureEmail;

    /**
     * Update multiple KYC records individually with trackId-wise requestId, shortUrl, and signingUrl
     */
    public void updateVoterIdResponseBatch(List<KycData> kycEntities,
                                           VoterIdResponseToMerchant response,
                                           JSONObject input,
                                           boolean isSuccess,
                                           Map<String, String> trackIdToRequestId,
                                           Long productRate,
                                           Map<String, String> trackIdToShortUrl,
                                           Map<String, String> trackIdToSigningUrl) {
        try {
            if (kycEntities == null || kycEntities.isEmpty()) {
                log.warn("No KYC records to update.");
                return;
            }

            String responseJson = response != null ? new ObjectMapper().writeValueAsString(response) : null;

            for (KycData entity : kycEntities) {
                String trackId = entity.getTrackId();

                entity.setRequestId(trackIdToRequestId != null ? trackIdToRequestId.get(trackId) : null);
                entity.setSdkUrl(trackIdToSigningUrl != null ? trackIdToSigningUrl.get(trackId) : null);
                entity.setShortUrl(trackIdToShortUrl != null ? trackIdToShortUrl.get(trackId) : null);
                entity.setMerchantResponseAt(java.time.LocalDateTime.now());
                entity.setMerchantResponse(responseJson);
                entity.setProviderResponse(input.toString());
                entity.setStatus(isSuccess ? STATUS_SUCCESS : STATUS_FAILED);
                entity.setBillable(isSuccess ? "Y" : "N");
                entity.setProductRate(productRate);
            }

            repository.saveAll(kycEntities);
            log.info("Batch update completed for {} trackIds.", kycEntities.size());

            if (!isSuccess) {
                for (KycData entity : kycEntities) {
                    sendFailureEmailForEntity(entity);
                }
            }

        } catch (Exception e) {
            log.error("Error in batch update of voterId response.", e);
        }
    }

    private void sendFailureEmailForEntity(KycData entity) {
        try {
            String merchantId = nullToEmpty(entity.getMerchantId());
            String merchantName = nullToEmpty(entity.getMerchantName());
            String verificationType = nullToEmpty(entity.getProduct());
            String reasonMessage = nullToEmpty(entity.getReasonMessage());
            String requestTime = entity.getMerchantRequestAt() != null
                    ? entity.getMerchantRequestAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                    : "";

            String mailBody = mailString(merchantId, merchantName, entity.getTrackId(), verificationType, reasonMessage,
                    requestTime);
            String subject = failuredlVerifySubject.replace("{productName}", verificationType);

            sendFailureEmail.sendEkycFailureEmail(mailBody, entity.getTrackId(), subject, SendTo);
        } catch (Exception e) {
            log.error("Error sending failure email for trackId: {}", entity.getTrackId(), e);
        }
    }

    public String mailString(String merchantId, String merchantName, String trackId, String verificationType,
                             String reason, String requestTime) throws IOException {
        String mailstring = CommonUtils.readUsingFileInputStream(emailTemplatePath);
        mailstring = mailstring.replace("{{MerchantID}}", merchantId);
        mailstring = mailstring.replace("{{MerchantName}}", merchantName);
        mailstring = mailstring.replace("{{trackId}}", trackId);
        mailstring = mailstring.replace("{{kycType}}", verificationType);
        mailstring = mailstring.replace("{{errorDetails}}", reason);
        mailstring = mailstring.replace("{{requestTime}}", requestTime);
        return mailstring;
    }

    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }

}

