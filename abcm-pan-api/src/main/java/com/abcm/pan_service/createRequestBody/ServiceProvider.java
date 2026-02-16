package com.abcm.pan_service.createRequestBody;

import com.abcm.pan_service.dto.PanVerifyRequest;

public interface ServiceProvider<S> {
    S buildSendOtpRequestBody(PanVerifyRequest request);
   
}
