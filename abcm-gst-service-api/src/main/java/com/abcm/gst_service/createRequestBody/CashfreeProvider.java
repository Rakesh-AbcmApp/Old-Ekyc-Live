package com.abcm.gst_service.createRequestBody;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.abcm.gst_service.dto.MerchantGstMasterRequest;

@Service("cashfree")
public class CashfreeProvider implements ServiceProvider<Map<String, Object>> {

    @Override
    public Map<String, Object> buildGstinRequestBody(MerchantGstMasterRequest request) {
        // Creating the JSON body as a Map with the key "GSTIN"
        return Map.of(
            "GSTIN", request.getBusinessGstinNumber()
        );
    }
}
