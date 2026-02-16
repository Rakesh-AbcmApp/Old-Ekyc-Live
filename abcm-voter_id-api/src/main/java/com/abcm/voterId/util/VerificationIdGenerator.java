package com.abcm.voterId.util;

import java.util.UUID;

public class VerificationIdGenerator {

    public static String generateVerificationId() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.length() > 50 ? uuid.substring(0, 50) : uuid;
    }
}