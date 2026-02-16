package com.abcm.dl_service.createRequestBody;

import org.springframework.stereotype.Service;
import com.abcm.dl_service.dto.DlVerifyRequest;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("zoop")
@Slf4j
public class ZoopProvider implements ServiceProvider<Map<String, Object>> {

   
    public Map<String, Object> buildDlRequestBody(DlVerifyRequest request) {
        log.info("inside zoop provider dynamic DL request");
        Map<String, Object> data = new HashMap<>();
        data.put("customer_dl_number", request.getCustomer_dl_number());
        data.put("customer_dob", request.getCustomer_dob()); // format: DD-MM-YYYY
        data.put("consent", request.getConsent());
        data.put("consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API");
        Map<String, Object> root = new HashMap<>();
        root.put("mode", "sync");
        root.put("task_id", generateTaskId());
        root.put("data", data);
        return root;
    }
    
    private String generateTaskId() {
		return UUID.randomUUID().toString(); // Generates a random UUID as a string
	}

	
}
