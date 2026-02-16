package com.abcm.esign_service.dyanamicRequestBody;

import org.springframework.web.multipart.MultipartFile;

import com.abcm.esign_service.DTO.EsignMerchantRequest;

public interface ServiceProvider<S> {

	ZoopEsignAdhaarRequest buildRequest(EsignMerchantRequest request,MultipartFile file);
}
