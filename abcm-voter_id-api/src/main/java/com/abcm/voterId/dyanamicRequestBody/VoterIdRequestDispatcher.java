package com.abcm.voterId.dyanamicRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.voterId.DTO.VoterIdRequestModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoterIdRequestDispatcher {

	private final Map<String, ServiceProvider<Map<String, Object>>> providers;

	public Map<String, Object> VoterIdProviderRequestBody(String providerName, VoterIdRequestModel request) {
		ServiceProvider<Map<String, Object>> provider = providers.get(providerName.toLowerCase());
		if (provider == null)
			throw new RuntimeException("Provider not found: " + providerName);
		return provider.buildRequest(request);
	}

}
