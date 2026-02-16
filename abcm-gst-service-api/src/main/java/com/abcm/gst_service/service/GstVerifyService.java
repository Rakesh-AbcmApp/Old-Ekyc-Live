package com.abcm.gst_service.service;

import com.abcm.gst_service.dto.ResponseModel;
import com.abcm.gst_service.dto.MerchantGstMasterRequest;

public interface GstVerifyService {

	public ResponseModel processGstLiteVerification(MerchantGstMasterRequest gstMasterRequest, String appId,
			String apiKey);

	public ResponseModel processGstAdvanceVerification(MerchantGstMasterRequest gstMasterRequest, String appId,
			String apiKey);
	
	

}
