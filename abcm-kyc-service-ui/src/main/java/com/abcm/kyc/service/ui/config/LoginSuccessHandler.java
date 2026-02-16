package com.abcm.kyc.service.ui.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final Environment environment;
	

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // ✅ Set session flag
    	HttpSession session = request.getSession(true);
        session.setAttribute("LOGGED_IN", true);
        session.setAttribute("loginTime", System.currentTimeMillis());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/login?error"; // Default fallback
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            String ContextPathUrl=environment.getProperty("ContextPath");
            log.info("Login Success with role: {}", role);
            log.info("Context Path: {}", environment.getProperty("ContextPath"));
            switch (role) {
                case "ROLE_ADMIN":
                    redirectUrl = ContextPathUrl+"/app/admin/dashboard";
                    break;
                case "ROLE_MERCHANT":
                    redirectUrl = ContextPathUrl+"/app/merchant/dashboard";
                    break;
                default:
                    redirectUrl = ContextPathUrl+"/login";
                    break;
            }
            break; // Only use first role
        }

        response.sendRedirect(redirectUrl);
    }
}

