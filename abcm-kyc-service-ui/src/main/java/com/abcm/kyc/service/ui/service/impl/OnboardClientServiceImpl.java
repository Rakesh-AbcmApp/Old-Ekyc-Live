package com.abcm.kyc.service.ui.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abcm.kyc.service.ui.dto.ApiResponseModel;
import com.abcm.kyc.service.ui.dto.OnboardClientRequest;
import com.abcm.kyc.service.ui.repository.OnboardClientRepo;
import com.abcm.kyc.service.ui.service.OnboardClientService;
import com.abcm.kyc.service.ui.util.ResponseUtil;
import com.abcmkyc.entity.ClientMaster;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardClientServiceImpl implements OnboardClientService {

	private final OnboardClientRepo clientRepo;
	private final ResponseUtil responseUtil;

	@Override
	@Transactional
	@Async
	public ApiResponseModel onboardClient(OnboardClientRequest clientRequest) {
	    try {
	        log.info("clientRequest: {}", clientRequest);
	        ClientMaster client = clientRepo.findById(clientRequest.getId());
	        boolean isNew = (client == null);
	        if (isNew) {
	            client = new ClientMaster();
	            client.setApiKey(clientRequest.getApiKey()); // Only for new
	        }
	        mapClientDetails(client, clientRequest);
	        client.setCreatedOn(LocalDateTime.now());
	        client.setStatus(true);
	        clientRepo.save(client);
	        log.info("Client {}: {}", isNew ? "created" : "updated", client);
	        return responseUtil.build(isNew ? "PROVIDER_ONBOARD_SUCCESS" : "PROVIDER_UPDATE_SUCCESS", client);

	    } catch (Exception ex) {
	        log.error("Error occurred while onboarding client: ", ex);
	        throw new RuntimeException("Failed to onboard client", ex);
	    }
	}

	private void mapClientDetails(ClientMaster client, OnboardClientRequest req) {
	    client.setServiceProviderName(req.getProviderName());
	    client.setApiAadharUrl1(req.getAaddharUrl1());
	    client.setApiAadharUrl2(req.getAaddharUrl2());
	    client.setApiGstUrl1(req.getGstUrl1());
	    //client.setApiGstUrl2(req.getGstUrl2());
	    client.setApiPanUrl1(req.getPanUrl1());
	    //client.setApiPanUrl2(req.getPanUrl2());
	    client.setDrivingLsUrl(req.getDrivingLicense());
	    client.setVoterIdUrl(req.getVoterId());
	    client.setUdf1(req.getUdf1());
	    client.setUdf2(req.getUdf2());
	    client.setUdf3(req.getUdf3());
	    client.setUdf4(req.getUdf4());
	    client.setUdf5(req.getUdf5());
	    client.setAppId(req.getApiId());
	    client.setApiKey(req.getApiKey());
	    client.setEnvironment(req.getEnvironment());
	}

	
	@Override
	public ApiResponseModel onboardClientReport() {
		List<ClientMaster> activeClients = clientRepo.findByStatusTrue();
	    return responseUtil.build("FETCH_SUCCESS", activeClients);
	}
	
	
	@Override
	public ApiResponseModel findbyId(long id) {
	    try {
	        ClientMaster client = clientRepo.findById(id);
	        if (client != null) {
	            return responseUtil.build("FETCH_SUCCESS", client);
	        } else {
	            return responseUtil.build("NOT_FOUND", null);
	        } 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return responseUtil.build("INTERNAL_ERROR", null);
	    }
	}
}
