package com.abcm.addhar_service.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampUtil {

    public static String getISTTimestamp() {
        OffsetDateTime nowIST = OffsetDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return nowIST.format(formatter);
    }
    
    
    
    public static String convertUtcToIst(String utcTimestamp) {
        Instant instant = Instant.parse(utcTimestamp);
        OffsetDateTime istTime = instant.atZone(ZoneId.of("Asia/Kolkata")).toOffsetDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return formatter.format(istTime);
    }

    public static void main(String[] args) {
        String timestamp = getISTTimestamp();
        System.out.println(timestamp);  // ✅ e.g., 2025-08-23T15:57:58.520+05:30
    }
}