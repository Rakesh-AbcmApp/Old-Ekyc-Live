package com.abcm.dl_service.dyanamicProviderResponse;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ResponseDispatcher {

    private final Map<String, ProviderResponseHandler<?>> handlers = new HashMap<>();

    @Autowired
    public ResponseDispatcher(
        @Lazy ZoopResponseHandler zoopResponseHandler,
        @Lazy CashfreeResponseHandler cashfreeResponseHandler
    ) {
        handlers.put("zoop", zoopResponseHandler);
        handlers.put("cashfree", cashfreeResponseHandler);
    }

    @SuppressWarnings("unchecked")
    public <T> T DlResponse(String providerName, JSONObject input, String trackId, String merchanId, Long productRate,String orderId) {
        System.out.println("ResponseDispatcher Enter: " + providerName);

        ProviderResponseHandler<T> handler = (ProviderResponseHandler<T>) handlers.get(providerName.toLowerCase());
        if (handler == null) {
            throw new RuntimeException("No response handler found for provider: " + providerName);
        }

        return handler.DlVerifyResponseToMerchant(input, trackId, merchanId, productRate,orderId);
    }
}
