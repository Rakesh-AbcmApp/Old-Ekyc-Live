package com.abcm.dl_service.service;

import com.abcm.dl_service.dto.DlVerifyRequest;
import com.abcm.dl_service.dto.ResponseModel;

public interface DlVerifyService {
	
public ResponseModel processDlVerification(DlVerifyRequest panVerifyRequest, String appId, String apiKey);


}
