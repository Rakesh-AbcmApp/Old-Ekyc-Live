package com.abcm.dl_service.createRequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import com.abcm.dl_service.dto.DlVerifyRequest;
import com.abcm.dl_service.util.VerificationIdGenerator;

@Service("cashfree")
public class CashfreeProvider implements ServiceProvider<Map<String, Object>> {

    @Override
    public Map<String, Object> buildDlRequestBody(DlVerifyRequest request) {
        String verificationId = VerificationIdGenerator.generateVerificationId();
        String dobFormatted = "";

        try {
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(request.getCustomer_dob(), inputFormat);
            dobFormatted = dob.format(outputFormat);
        } catch (DateTimeParseException e) {
            System.err.println("❌ Invalid DOB format: " + request.getCustomer_dob());
        }

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("verification_id", verificationId);
        requestBody.put("dl_number", request.getCustomer_dl_number());
        requestBody.put("dob", dobFormatted);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            System.out.println("✅ Generated RequestJSON Cashfree:\n" + prettyJson);
        } catch (Exception e) {
            System.err.println("❌ Failed to convert request body to JSON: " + e.getMessage());
        }

        return requestBody;
    }
}
