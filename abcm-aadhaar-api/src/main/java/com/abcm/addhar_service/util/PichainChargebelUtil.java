package com.abcm.addhar_service.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PichainChargebelUtil {

    // Map to store chargeable code-message pairs
    private static final Map<String, Set<String>> CHARGEABLE_STATUS_MAP = new HashMap<>();

    static {
        CHARGEABLE_STATUS_MAP.put("2200", Set.of("Offline Aadhaar initiated"));
        CHARGEABLE_STATUS_MAP.put("4004", Set.of("Offline Aadhar Process Failed - Aadhar Number Not Found"));
        CHARGEABLE_STATUS_MAP.put("2201", Set.of(
            "Offline Aadhar Process Failed - Aadhar Number Not Found",
            "Offline Aadhaar Initiate Failed :Aadhaar number not linked to mobile number"
        ));
        CHARGEABLE_STATUS_MAP.put("4003", Set.of("Offline Aadhar Process Failed - Aadhar Number Not Found"));
        CHARGEABLE_STATUS_MAP.put("4001", Set.of("Invalid Request, required Valid OrgId"));
    }

   
    public boolean isChargeable(String statusCode, String message) {
        log.info("Inside charge deduction utility method");

        if (statusCode == null || message == null) {
            return false;
        }

        String cleanedCode = statusCode.trim();
        String cleanedMessage = message.trim();

        log.info("Code: [{}], Message: [{}]", cleanedCode, cleanedMessage);

        Set<String> validMessages = CHARGEABLE_STATUS_MAP.get(cleanedCode);

        boolean result = validMessages != null && validMessages.stream()
            .anyMatch(msg -> cleanedMessage.equalsIgnoreCase(msg.trim()));

        log.info("Is chargeable? {}", result);

        return result;
    }
}
