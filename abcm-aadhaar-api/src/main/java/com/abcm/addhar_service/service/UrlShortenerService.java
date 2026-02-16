package com.abcm.addhar_service.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
    private final Map<String, String> urlMap = new ConcurrentHashMap<>();

    public String shorten(String longUrl) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        urlMap.put(code, longUrl);
        return code;
    }

    public String getLongUrl(String code) {
        return urlMap.get(code);
    }
}