package com.abcm.addhar_service.Email;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
public class SendFailureEmail {

    @Value("${email.send.from}")
    private String from;

   

    @Value("${email.send.bcc}")
    private String bcc;

    @Value("${email.send.api.url}")
    private String emailApiUrl;

   
    

    
    public void sendAadhaarFailureEmail(String mailString, String tracktId, String subject,String SendTo) {
        try {
            log.info("Email Aadhaar Failed Trigger: {}", subject);

            String finalSubject = (tracktId == null || tracktId.trim().isEmpty())
                    ? subject
                    : subject + " - " + tracktId;
            
            JSONArray bccArray = new JSONArray();
            if (bcc != null && !bcc.isBlank()) {
                for (String email : bcc.split(",")) {
                    bccArray.put(email.trim());
                }
            }
            JSONObject requestBody = new JSONObject();
            requestBody.put("from", from);
            requestBody.put("to", SendTo);
            requestBody.put("bcc", bccArray);
            requestBody.put("subject", finalSubject);
            requestBody.put("contentType", "text/html");
            requestBody.put("body", mailString);
            String emailResponse = WebClient.create()
            		.post()
                    .uri(emailApiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody.toString()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(ex -> log.error("WebClient Email Error: {}", ex.getMessage()))
                    .onErrorReturn("fail:false")
                    .block();

            log.info("Aadhaar Email Sent: TrackID={}, Response={}", tracktId, emailResponse);

        } catch (Exception e) {
            log.error("Exception in sendAadhaarFailureEmail for TrackID={}: {}", tracktId, e.getMessage(), e);
        }
    }
}
