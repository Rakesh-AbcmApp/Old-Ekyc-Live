package com.abcm.esign_service.dyanamicRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.esign_service.DTO.EsignMerchantRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EsignRequestDispatcher {

	private final Map<String, ServiceProvider<ZoopEsignAdhaarRequest>> providers;

	public ZoopEsignAdhaarRequest EsignProviderRequestBody(String providerName, EsignMerchantRequest request,
			MultipartFile multipartFile) {
		log.info("Provider Name is: {}", providerName.toLowerCase());
		ServiceProvider<ZoopEsignAdhaarRequest> provider = providers.get(providerName.toLowerCase());

		if (provider == null) {
			throw new RuntimeException("Provider not found: " + providerName);
		}
		return provider.buildRequest(request, multipartFile);
	}
}
