package com.abcm.dl_service.createRequestBody;

import com.abcm.dl_service.dto.DlVerifyRequest;

public interface ServiceProvider<S> {
    S buildDlRequestBody(DlVerifyRequest request);
   
}
