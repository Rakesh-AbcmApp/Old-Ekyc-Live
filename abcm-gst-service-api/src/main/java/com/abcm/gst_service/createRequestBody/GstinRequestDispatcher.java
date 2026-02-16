package com.abcm.gst_service.createRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.gst_service.dto.MerchantGstMasterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GstinRequestDispatcher {

    private final Map<String, ServiceProvider<Map<String,Object>>> providers;

    public Map<String,Object> GstinProviderRequestBody(String providerName, MerchantGstMasterRequest request) {
        ServiceProvider<Map<String,Object>> provider = providers.get(providerName.toLowerCase());
        if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        return provider.buildGstinRequestBody(request);
    }


   
}

