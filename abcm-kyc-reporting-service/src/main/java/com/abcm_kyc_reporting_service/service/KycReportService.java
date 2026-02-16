package com.abcm_kyc_reporting_service.service;

import java.util.Date;

import com.abcm_kyc_reporting_service.dto.KycApiCountResponseModel;
import com.abcm_kyc_reporting_service.dto.KycReportRequestModel;
import com.abcm_kyc_reporting_service.dto.ResponseModel;
import com.abcm_kyc_reporting_service.dto.StatusCheckRequest;
public interface KycReportService {

	

	public ResponseModel fetchReportOkyc(KycReportRequestModel kycReportRequestModel);
	
	public ResponseModel fetchReportOkycMerchant(KycReportRequestModel kycReportRequestModel);

	public KycApiCountResponseModel fetchReportOkyc(Date startDate, Date endDate, String merchantId, String product);

	public ResponseModel checkStatus(String appId, String apiKey, StatusCheckRequest statusCheckRequest);

}
