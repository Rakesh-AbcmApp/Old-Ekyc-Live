package com.abcm.addhar_service.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.abcm.addhar_service.service.DigiService;
import com.abcm.addhar_service.service.UrlShortenerService;
import com.abcmkyc.entity.KycData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
public class AadharKycApiCommanController {

	@Autowired 
	DigiService digiService;
	
	@Autowired
    private UrlShortenerService urlShortenerService;
	
	
	
	@GetMapping("/r/{code}")
    public void redirect(@PathVariable("code") String code, HttpServletResponse response) throws IOException {
        log.info("longUrl ::"+code);
        String longUrl = urlShortenerService.getLongUrl(code);
        log.info("longUrl ::"+longUrl);
        if (longUrl != null) {
            response.sendRedirect(longUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
	
	
	
	
	@RequestMapping(value = "/redirect", method = {RequestMethod.GET, RequestMethod.POST})
	public String handleRedirect(
			   @RequestParam(name = "status", required = false) String status,
		        @RequestParam(name = "txn_id", required = false) String txnId,
		        @RequestParam(name = "code", required = false) String code,
		        @RequestParam(name = "action", required = false) String action,
	        Model model) 
	{

	    log.info("Redirect received - status: {}, txn_id: {}, code: {}, action: {}", status, txnId, code, action);
	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    log.info("Full redirect URL: {}", request.getRequestURL() + "?" + request.getQueryString());

	    String message;

	    switch (action != null ? action : "") {
	        case "digilocker-success":
	            message = " Digilocker verification successful. ";
	            model.addAttribute("message",message);
	            
	            break;
	        case "digilocker-error":
	            message = " Digilocker verification failed. Please try again.";
	            model.addAttribute("message",message);
	            break;
	        case "consent-denied":
	            message = "Consent denied. The process cannot proceed without your approval.";
	            model.addAttribute("message",message);
	            break;
	        case "close":
	            message = "The session was closed manually.";
	            model.addAttribute("message",message);
	            break;
	        case "gateway-error":
	            message = "An unexpected error occurred during verification. Please try later.";
	            model.addAttribute("message",message);
	            break;
	        default:
	            message = "Redirected with unknown action. Please contact support.";
	            model.addAttribute("message",message);
	    }
	   	    
	    return "digiresponse";

	   
	}



	@PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> receiveWebhook(
	        @RequestBody String payload,
	        @RequestHeader(value = "webhook_security_key", required = false) String incomingKey) {

	    try {
	        log.info("Received Webhook Payload: {}", payload);
	        log.info("Received Webhook Security Key: {}", incomingKey);

	        if (incomingKey == null || incomingKey.isBlank()) {
	            log.warn("Missing or blank webhook_security_key");
	            return ResponseEntity.badRequest().body("Missing webhook_security_key header");
	        }

	        KycData entity = digiService.findByWebhookSecurityKey(incomingKey);

	        if (entity != null) {
	            String response = digiService.UpdateWebhookResponse(payload, incomingKey, entity);
	            log.info("Webhook updated successfully for key {}: {}", incomingKey, response);
	            return ResponseEntity.ok("Webhook received and processed.");
	        } else {
	            log.warn("No KYC entity found for webhook key: {}", incomingKey);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid webhook_security_key.");
	        }

	    } catch (Exception e) {
	        log.error("Error processing webhook", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
	    }
	}

	
	
	
	
	

}
