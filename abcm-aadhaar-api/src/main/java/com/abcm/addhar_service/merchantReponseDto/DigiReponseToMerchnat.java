package com.abcm.addhar_service.merchantReponseDto;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@ToString
@Builder(toBuilder = true)
public class DigiReponseToMerchnat {

    private String response_code;
    private String response_message;
    private boolean success;
    private String referenceId;
    private String track_id;
    private String merchant_id;

    @Builder.Default
    private String response_timestamp = OffsetDateTime.now(ZoneId.of("Asia/Kolkata"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

    private String sdkUrl;
    private String expiresAt;
    private String webhookSecurityKey;

    @Builder.Default
    private String requestTimestamp = OffsetDateTime.now(ZoneId.of("Asia/Kolkata"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

    private boolean digilockerSuccess;
    private String order_id;
}
