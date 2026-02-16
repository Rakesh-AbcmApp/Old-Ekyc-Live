package com.abcm.kyc.service.ui.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.abcm.kyc.service.ui.config.SecurityUtil;

@ControllerAdvice
public class GlobalExcptionHanlder {
	
	
	@ModelAttribute("authUser")
    public SecurityUtil.AuthenticatedUser getAuthenticatedUser() {
        return SecurityUtil.getAuthenticatedUser();
    }

}
