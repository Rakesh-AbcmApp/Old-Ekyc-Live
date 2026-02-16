package com.abcm.addhar_service.dyanamicReponse;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.abcmkyc.entity.KycData;


@Service
public class ResponseDispatcher {

    private final Map<String, ProviderResponseHandler<?>> handlers = new HashMap<>();

    @Autowired
    public ResponseDispatcher(
        @Lazy ZoopResponseHandler zoopResponseHandler,
        @Lazy CashfreeResponseHandler cashfreeResponseHandler,
        @Lazy DigiresponseHandler digiResponseHandler,
        @Lazy PichainlabsResponseHandler pichainlabsResponseHandler
    ) {
        handlers.put("zoop", zoopResponseHandler);
        handlers.put("cashfree", cashfreeResponseHandler);
        handlers.put("digilocker", digiResponseHandler);
        handlers.put("pichainlabs", pichainlabsResponseHandler);
    }


    @SuppressWarnings("unchecked")
    public <T> T getResponse(String providerName, JSONObject input, String trackId,String merchantId,Long productrate,String orderId) {
        System.out.println("ResponseDispatcher Enter 1{}:"+providerName);
        ProviderResponseHandler<T> handler = (ProviderResponseHandler<T>) handlers.get(providerName.toLowerCase());
        if (handler == null) {
            throw new RuntimeException("No response handler found for provider: " + providerName);
        }
        return handler.OTPSend(input, trackId,merchantId,productrate,orderId);
    }
    
    
    
    @SuppressWarnings("unchecked")
    public <T> T verifyOTP(String providerName, JSONObject input,String merchantId,Long productrate,KycData data) {
        System.out.println("ResponseDispatcher VerifyOTP Enter{}:" + providerName);
        ProviderResponseHandler<T> handler = (ProviderResponseHandler<T>) handlers.get(providerName.toLowerCase());
        if (handler == null) {
            throw new RuntimeException("No response handler found for provider: " + providerName);
        }
        return handler.OTPVerify(input,merchantId,productrate,data);
    }
    
    
 
    @SuppressWarnings("unchecked")
	public <T> T digiVerify(String providerName, JSONObject input, String trackId,String merchantId,String orderId) {
		 System.out.println("ResponseDispatcher digiVerify Enter{}:" + providerName);
	        ProviderResponseHandler<T> handler = (ProviderResponseHandler<T>) handlers.get(providerName.toLowerCase());
	        if (handler == null) {
	            throw new RuntimeException("No response handler found for provider: " + providerName);
	        }
	        return handler.digiVerify(input, trackId,merchantId,orderId);
	    
	}

    
}

