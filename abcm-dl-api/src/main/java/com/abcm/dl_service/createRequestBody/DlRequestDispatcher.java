package com.abcm.dl_service.createRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.dl_service.dto.DlVerifyRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DlRequestDispatcher {

    private final Map<String, ServiceProvider<Map<String,Object>>> providers;

    public Map<String,Object> DlProviderRequestBody(String providerName, DlVerifyRequest request) {
        ServiceProvider<Map<String,Object>> provider = providers.get(providerName.toLowerCase());
        if (provider == null) throw new RuntimeException("Provider not found: " + providerName);
        return provider.buildDlRequestBody(request);
    }


   
}

