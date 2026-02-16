package com.abcm.addhar_service.createRequestbody;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AadhaarRequestDispatcher {
	
	

    private final Map<String, ServiceProvider<Map<String,Object>, Map<String,Object>>> providers;

    public Map<String,Object> getSendOtpRequestBody(String providerName, AadhaarOtpRequest request) {
    	ServiceProvider<Map<String,Object>, Map<String,Object>> provider = providers.get(providerName.toLowerCase());
    	log.info(" AadhaarRequestDispatcher send otp  provider name"+provider);
        if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        return provider.buildSendOtpRequestBody(request);
    }

    public Map<String,Object> getVerifyOtpRequestBody(String providerName, VerifyOtpRequest request,String requestId) {
    	
    	
    	ServiceProvider<Map<String,Object>, Map<String,Object>> provider = providers.get(providerName.toLowerCase());
    	
    	log.info(" AadhaarRequestDispatcher verify  otp  provider name"+provider);
       
    	if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        
        return provider.buildVerifyOtpRequestBody(request,requestId);
    }

	public Map<String, Object> getDigiRequestBody(String providerName, ZoopInitRequest request) {
		
		log.info("getDigiRequestBody :"+providerName);
		
		ServiceProvider<Map<String,Object>, Map<String,Object>> provider = providers.get(providerName.toLowerCase());
        
		if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        
        return provider.buildDigiRequestBody(request);
        
	}
}

