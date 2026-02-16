package com.abcm.esign_service.esignWebhook;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MerchantWebhookResponseDto {

    private String response_code;
    private String response_message;
    private String merchant_id;
    private String billable;
    private Boolean success;
    private String order_id;
    private Signer signer;
    private List<Signer> other_signers;

    @Data
    @ToString
    public static class Signer {
        private String request_id;
        private String track_id;
        private String given_name;
        private String fetched_name;
        private float name_match_score;
        private String status; 
        private String signed_url;
    }
}