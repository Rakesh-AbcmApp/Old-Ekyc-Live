package com.abcm.gst_service.dyanamicProviderResponse;



import org.json.JSONObject;

public interface ProviderResponseHandler<T> {
    T GstliteVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId,Long productRate, String orderId);
}
