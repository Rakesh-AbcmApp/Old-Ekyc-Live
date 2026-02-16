package com.abcm.kyc.service.ui.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AbsoluteSessionTimeoutFilter implements Filter {

    // 40 minutes of inactivity
    private static final long MAX_INACTIVE_INTERVAL = 30 * 60 * 1000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException { 
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();

        // Bypass static resources and login/logout pages
        if (uri.contains("/login") || uri.contains("/assets") || uri.contains("/logout") || uri.contains("/error")) {
            chain.doFilter(request, response);
            return;
        }
        if (session != null) {
            Long lastAccessedTime = (Long) session.getAttribute("loginTime");
            long now = System.currentTimeMillis();

            if (lastAccessedTime != null) {
                if ((now - lastAccessedTime) > MAX_INACTIVE_INTERVAL) {
                    session.invalidate();
                    httpResponse.sendRedirect("/login?session=expired");
                    return;
                }
            }
            // Update the last accessed time for next request
            session.setAttribute("lastAccessedTime", now);
        }

        chain.doFilter(request, response);
    }
}
