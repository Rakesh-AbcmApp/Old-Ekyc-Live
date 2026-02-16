package com.abcm.pan_service.createRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.pan_service.dto.PanVerifyRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PanRequestDispatcher {

    private final Map<String, ServiceProvider<Map<String,Object>>> providers;

    public Map<String,Object> PanProviderRequestBody(String providerName, PanVerifyRequest request) {
        ServiceProvider<Map<String,Object>> provider = providers.get(providerName.toLowerCase());
        if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        return provider.buildSendOtpRequestBody(request);
    }


   
}

