package com.abcm.kyc.service.ui.config;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtil {
    public static AuthenticatedUser getAuthenticatedUser() {
    	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        User userDetails = (User) authentication.getPrincipal();
        return new AuthenticatedUser(userDetails.getUsername(), userDetails.getAuthorities());
    }

    public static class AuthenticatedUser {
        private String username;
        private Collection<? extends GrantedAuthority> authorities;

        public AuthenticatedUser(String username, Collection<? extends GrantedAuthority> authorities) {
            this.username = username;
            this.authorities = authorities;
        }

        public boolean hasRole(String role) {
        	//System.out.println("The role is"+"ROLE_" + role);
            return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(role));
        }

        public String getUsername() {
            return username;
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }
    }
}
