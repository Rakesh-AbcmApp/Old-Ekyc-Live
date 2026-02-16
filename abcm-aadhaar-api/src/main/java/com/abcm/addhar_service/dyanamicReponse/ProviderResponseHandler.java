package com.abcm.addhar_service.dyanamicReponse;



import org.json.JSONObject;

import com.abcmkyc.entity.KycData;

public interface ProviderResponseHandler<T> {
	T OTPSend(JSONObject responseObj, String trackId,String merchantId,Long productrate,String orderId);
	T OTPVerify(JSONObject responseObj,String merchantId, Long productrate, KycData kycData);
	T digiVerify(JSONObject input, String trackId, String merchantId,String orderId);

}