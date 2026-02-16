package com.abcm.pan_service.service;

import com.abcm.pan_service.dto.PanVerifyRequest;
import com.abcm.pan_service.dto.ResponseModel;

public interface PanVerifyService {
	
public ResponseModel processPanVerification(PanVerifyRequest panVerifyRequest, String appId, String apiKey);


}
