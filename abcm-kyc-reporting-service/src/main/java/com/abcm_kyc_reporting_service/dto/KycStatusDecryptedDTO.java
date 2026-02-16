package com.abcm_kyc_reporting_service.dto;

import com.abcm_kyc_reporting_service.mapperModel.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.*;
import java.util.regex.*;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycStatusDecryptedDTO {

    private final String product;
    private final Object ProviderResponse;

    public KycStatusDecryptedDTO(String product, String raw) {
        this.product = product;
        this.ProviderResponse = parse(product, raw);
    }

    private Object parse(String product, String raw) {
        try {
            if (raw == null || raw.isBlank()) return Map.of("error", "Empty input");

            ObjectMapper mapper = new ObjectMapper();

            // JSON input
            if (raw.trim().startsWith("{")) {
                return switch (product.toUpperCase()) {
                    case "PAN" -> mapper.readValue(raw, PanResponseToMerchant.class);
                    case "GSTIN" -> mapper.readValue(raw, MerchantToGstinResponse.class);
                    case "DRIVING_LICENSE" -> mapper.readValue(raw, DlResponseToMerchant.class);
                    default -> mapper.readValue(raw, Map.class);
                };
            }

            // .toString() format fallback
            if (raw.contains("=")) {
                return parseToMap(raw);
            }

            return Map.of("error", "Unsupported format", "raw", raw);
        } catch (Exception e) {
            return Map.of("error", "Deserialization failed", "raw", raw);
        }
    }

    private Map<String, Object> parseToMap(String raw) {
        int start = raw.indexOf('(');
        int end = raw.lastIndexOf(')');
        if (start < 0 || end <= start) return Map.of("error", "Invalid format", "raw", raw);

        String content = raw.substring(start + 1, end);
        return parseFields(content);
    }

    private Map<String, Object> parseFields(String content) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String field : splitFields(content)) {
            String[] kv = field.split("=", 2);
            if (kv.length != 2) continue;

            String key = kv[0].trim();
            String value = kv[1].trim();

            if (value.endsWith(")") && value.contains("(")) {
                map.put(key, parseFields(value.substring(value.indexOf("(") + 1, value.lastIndexOf(")"))));
            } else if (value.startsWith("[") && value.contains("(")) {
                List<Map<String, Object>> list = new ArrayList<>();
                Matcher matcher = Pattern.compile("\\(([^()]+)\\)").matcher(value);
                while (matcher.find()) {
                    list.add(parseFields(matcher.group(1)));
                }
                map.put(key, list);
            } else if (!"null".equalsIgnoreCase(value) && !value.isBlank()) {
                map.put(key, value);
            }
        }
        return map;
    }

    private List<String> splitFields(String content) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;
        for (char c : content.toCharArray()) {
            if (c == ',' && depth == 0) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                if (c == '(') depth++;
                else if (c == ')') depth--;
                current.append(c);
            }
        }
        if (current.length() > 0) fields.add(current.toString());
        return fields;
    }
}
