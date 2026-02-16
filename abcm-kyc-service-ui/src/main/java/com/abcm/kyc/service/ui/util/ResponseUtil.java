package com.abcm.kyc.service.ui.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abcm.kyc.service.ui.dto.ApiResponseModel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseUtil {
    private final Environment env;
    public ApiResponseModel build(String key, Object data) {
        int code = Integer.parseInt(env.getProperty("response.codes." + key + ".code", "500"));
        String message = env.getProperty("response.codes." + key + ".message", "Unknown error");
        return new ApiResponseModel(code, message, data);
    }

    // Overload: if you just want message + code without data
    public ApiResponseModel build(String key) {
        return build(key, null);
    }

	
}
