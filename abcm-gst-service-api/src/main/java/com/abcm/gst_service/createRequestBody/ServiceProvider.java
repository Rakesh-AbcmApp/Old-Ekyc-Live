package com.abcm.gst_service.createRequestBody;

import com.abcm.gst_service.dto.MerchantGstMasterRequest;

public interface ServiceProvider<S> {
    S buildGstinRequestBody(MerchantGstMasterRequest request);
   
}
