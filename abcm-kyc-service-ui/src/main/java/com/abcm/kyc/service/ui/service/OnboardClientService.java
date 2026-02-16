package com.abcm.kyc.service.ui.service;

import com.abcm.kyc.service.ui.dto.ApiResponseModel;
import com.abcm.kyc.service.ui.dto.OnboardClientRequest;

public interface OnboardClientService {
	
	
	public ApiResponseModel onboardClient(OnboardClientRequest clientRequest);

	public ApiResponseModel onboardClientReport();
	
	public ApiResponseModel findbyId(long id);

}