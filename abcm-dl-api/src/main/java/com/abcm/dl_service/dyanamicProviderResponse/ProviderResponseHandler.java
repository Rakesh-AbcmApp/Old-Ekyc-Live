package com.abcm.dl_service.dyanamicProviderResponse;



import org.json.JSONObject;

public interface ProviderResponseHandler<T> {
    T DlVerifyResponseToMerchant(JSONObject responseObj, String trackId, String merchantId,Long productRate,String orderId);
}
