package com.abcm.kyc.service.ui.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class HSTSFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Content Security Policy
        //  httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' https://bbps.abcmapp.com; object-src 'none'; style-src 'self' https://bbps.abcmapp.com;");


        // Referrer Policy
        httpResponse.setHeader("Referrer-Policy", "no-referrer-when-downgrade");

        // Permissions Policy
        httpResponse.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}