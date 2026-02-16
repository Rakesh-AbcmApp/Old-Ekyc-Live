/*
package com.abcm.addhar_service.apicall;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceProviderApiCall {

    @Autowired
    private RestTemplate restTemplate;

    public String providerApiCall(Map<String, Object> requestBody, String apiKey, String appId, String apiAadharUrl, String provider) {
        log.info("Provider RestTemplate Call: {}", apiAadharUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("close");
        setProviderHeaders(headers, provider, appId, apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                apiAadharUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            int statusCode = response.getStatusCode().value();
            log.info("Received status code: {}", statusCode);

            if (statusCode == 503 || statusCode == 504) {
                log.error("5xx error response: {}", response.getBody());
                return "fail:false";
            }
            return response.getBody();
        } catch (Exception ex) {
            log.error("Exception calling third-party API: {}", ex.getMessage(), ex);
            return "fail:false";
        }
    }

    private void setProviderHeaders(HttpHeaders headers, String provider, String appId, String apiKey) {
        log.info("Provider setProviderHeaders: {}, {}, {}", provider, appId, apiKey);
        switch (provider.toLowerCase()) {
            case "zoop" -> {
                headers.set("app-id", appId);
                headers.set("api-key", apiKey);
            }
            case "cashfree" -> {
                headers.set("x-client-id", appId);
                headers.set("x-client-secret", apiKey);
            }
            case "digilocker" -> {
                headers.set("app-id", appId);
                headers.set("api-key", apiKey);
            }
            case "pichainlabs" -> {
                headers.set("org-id", appId);
                headers.set("apikey", apiKey);
            }
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}
*/









package com.abcm.addhar_service.apicall;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceProviderApiCall {

    @Autowired
    private RestTemplate restTemplate;

    private static final int MAX_RETRIES = 3;      // max retry attempts
    private static final long RETRY_DELAY_MS = 2000; // 2 sec delay between retries

    public String providerApiCall(Map<String, Object> requestBody, String apiKey, String appId, String apiAadharUrl, String provider) {
        log.info("Provider RestTemplate Call: {}", apiAadharUrl);
        HttpHeaders headers = new HttpHeaders();
        setProviderHeaders(headers, provider, appId, apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setConnection("close"); // 👈 important: force close connection after request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
            	log.info("============apicall===============");
                attempt++;
                log.info("Attempt {} calling API: {}", attempt, apiAadharUrl);
                ResponseEntity<String> response = restTemplate.exchange(
                        apiAadharUrl,
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                int statusCode = response.getStatusCode().value();
                log.info("Received status code: {}", statusCode);
                if (statusCode == 503 || statusCode == 504) {
                   // log.error("5xx error response: {}", response.getBody());
                    //throw new RuntimeException("5xx error from provider"); // retry for 5xx
                    return "fail:false";
                }
                return response.getBody(); // ✅ success

            } catch (Exception ex) {
                log.error("Exception on attempt {} calling third-party API: {}", attempt, ex.getMessage());
                if (attempt >= MAX_RETRIES) {
                    log.error("Max retries reached. Failing request.");
                    return "fail:false";
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS); // wait before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return "fail:false";
    }

    private void setProviderHeaders(HttpHeaders headers, String provider, String appId, String apiKey) {
        log.info("Provider setProviderHeaders: {}, {}, {}", provider, appId, apiKey);
        switch (provider.toLowerCase()) {
            case "zoop" -> {
                headers.set("app-id", appId);
                headers.set("api-key", apiKey);
            }
            case "cashfree" -> {
                headers.set("x-client-id", appId);
                headers.set("x-client-secret", apiKey);
            }
            case "digilocker" -> {
                headers.set("app-id", appId);
                headers.set("api-key", apiKey);
            }
            case "pichainlabs" -> {
                headers.set("org-id", appId);
                headers.set("apikey", apiKey);
            }
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}