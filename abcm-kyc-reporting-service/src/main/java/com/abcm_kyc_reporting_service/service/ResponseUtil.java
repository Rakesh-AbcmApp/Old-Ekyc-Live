package com.abcm_kyc_reporting_service.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> parseJsonOrFallback(String raw, ObjectMapper mapper) {
        if (raw == null || raw.isBlank()) return null;

        try {
            // Try parsing as JSON
            if (raw.trim().startsWith("{")) {
                return mapper.readValue(raw, Map.class);
            }

            // Fallback: Parse from .toString() format
            Map<String, String> parsed = ToStringParser.parse(raw);
            return new LinkedHashMap<>(parsed); // cast to Object values
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Failed to parse: " + e.getMessage());
            return error;
        }
    }
    
    public class ToStringParser {
        public static Map<String, String> parse(String input) {
            Map<String, String> map = new LinkedHashMap<>();
            if (input == null || !input.contains("{") || !input.contains("}")) return map;

            int start = input.indexOf("{");
            int end = input.lastIndexOf("}");
            String keyValuePart = input.substring(start + 1, end);

            String[] pairs = keyValuePart.split(", (?=[^=]+=)");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    map.put(kv[0].trim(), kv[1].trim().replace("'", "\""));
                }
            }
            return map;
        }
    }

}
