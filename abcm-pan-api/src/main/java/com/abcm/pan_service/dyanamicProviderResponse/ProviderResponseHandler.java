package com.abcm.pan_service.dyanamicProviderResponse;



import org.json.JSONObject;

public interface ProviderResponseHandler<T> {
    T PanVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId,Long productRate,String orderId);
}
