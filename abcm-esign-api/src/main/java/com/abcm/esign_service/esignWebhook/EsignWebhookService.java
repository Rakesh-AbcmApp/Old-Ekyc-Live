package com.abcm.esign_service.esignWebhook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.abcm.esign_service.repo.EsignRepository;
import com.abcmkyc.entity.KycData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EsignWebhookService {
    private final EsignRepository esignRepository;

    @Transactional
    public void updateWebhookResponse(String response) {
        JSONObject json = new JSONObject(response);
        JSONObject result = json.getJSONObject("result");
        JSONObject document = result.getJSONObject("document");
        log.info("document Status is: {}", document.getString("status"));
        String mainRequestId = json.getString("request_id");
        List<String> requestIds = new ArrayList<>();
        requestIds.add(mainRequestId);
        // Collect other signer requestIds
        if (result.has("other_signers")) {
            for (Object obj : result.getJSONArray("other_signers")) {
                JSONObject other = (JSONObject) obj;
                requestIds.add(other.getString("request_id"));
            }
        }
        // Single Database Fetch
        List<KycData> kycList = esignRepository.findByRequestIdIn(requestIds);
        Map<String, KycData> kycMap = kycList.stream()
                .collect(Collectors.toMap(KycData::getRequestId, k -> k));

        // Main Signer
        JSONObject signer = result.getJSONObject("signer");
        String mainStatus = signer.optString("status");
        KycData mainData = kycMap.get(mainRequestId);
        if (mainData != null) {
            // Only upgrade status if not already SUCCESS
            if (!"SUCCESS".equalsIgnoreCase(mainData.getSignerStatus())) {
                mainData.setSignerStatus(mainStatus);
                if ("SUCCESS".equalsIgnoreCase(mainStatus)) {
                    mainData.setStatus("SIGNED"); // Upgrade to SIGNED only if SUCCESS
                } else {
                    mainData.setStatus(document.getString("status"));
                }
            }
            mainData.setWebhookStatus(true);
           
        }

        // Update Other Signers
        if (result.has("other_signers")) {
            for (Object obj : result.getJSONArray("other_signers")) {
                JSONObject other = (JSONObject) obj;
                String otherRequestId = other.getString("request_id");
                String otherStatus = other.optString("status");
                KycData otherSignerData = kycMap.get(otherRequestId);
                if (otherSignerData != null) {
                    // Only upgrade status if not already SUCCESS
                    if (!"SUCCESS".equalsIgnoreCase(otherSignerData.getSignerStatus())) {
                        otherSignerData.setSignerStatus(otherStatus);
                        if ("SUCCESS".equalsIgnoreCase(otherStatus)) {
                            otherSignerData.setStatus("SIGNED");
                        } else {
                            otherSignerData.setStatus(document.getString("status"));
                        }
                    }
                }
            }
        }
        esignRepository.saveAll(kycList);
    }
}

