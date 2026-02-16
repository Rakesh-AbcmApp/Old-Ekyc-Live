package com.abcm.addhar_service.createRequestbody;

import java.util.Map;

import com.abcm.addhar_service.controller.ZoopInitRequest;
import com.abcm.addhar_service.dto.AadhaarOtpRequest;
import com.abcm.addhar_service.dto.VerifyOtpRequest;

public interface ServiceProvider<S, V> {
    S buildSendOtpRequestBody(AadhaarOtpRequest request);
    V buildVerifyOtpRequestBody(VerifyOtpRequest request, String requestId);
	S buildDigiRequestBody(ZoopInitRequest request);
}
