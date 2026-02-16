package com.abcm.addhar_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v3")
@RequiredArgsConstructor
@Slf4j
public class AdharMerchantResponseController {
	
	
	@RequestMapping(value = "/MerchantRedirect", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<String> handleRedirect(
	        @RequestParam(required = false) String action) {

	    log.info("Redirect received -  action: {}",action);
	   
	    String message;

	    switch (action != null ? action : "") {
	        case "digilocker-success":
	            message = " Digilocker verification successful. You may now close this window.";
	            break;
	        case "digilocker-error":
	            message = " Digilocker verification failed. Please try again.";
	            break;
	        case "consent-denied":
	            message = "Consent denied. The process cannot proceed without your approval.";
	            break;
	        case "close":
	            message = "The session was closed manually.";
	            break;
	        case "gateway-error":
	            message = "An unexpected error occurred during verification. Please try later.";
	            break;
	        default:
	            message = "Redirected with unknown action. Please contact support.";
	    }

	    return ResponseEntity.ok(message);
	}
	
	@PostMapping(value = "/MerchantWebhook")
	public ResponseEntity<String> receiveWebhook( @RequestBody String payload) {
	    try {
	        log.info("Received merchant webhook: {}", payload);

	       
	        // Respond success
	        return ResponseEntity.ok("Webhook received successfully");

	    } catch (Exception ex) {
	        log.error("Error processing merchant webhook", ex);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error processing webhook");
	    }
	}

	

}
