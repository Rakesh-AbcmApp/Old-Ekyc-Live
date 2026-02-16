package com.abcm.kyc.service.ui.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VerifyPassword {
    public static void main(String[] args) {
        String hash = "$2a$10$2s9zfHHJu7I6YNYCUbMXgeU7z02pm9PQ9DZlm48ijQOhoZ/oCvo6e";
        String password = "yourPlainTextPassword";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isMatch = encoder.matches(password, hash);
        System.out.println("Password match: " + isMatch);
    }
}
