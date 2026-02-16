package com.abcm.kyc.service.ui.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
	
	@Value("${ContextPath}")
	private String contextPath;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
    	log.info("Failurer Login"+contextPath);
    	response.sendRedirect(contextPath + "/login?error=invalid");
    }
}
