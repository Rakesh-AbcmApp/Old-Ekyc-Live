package com.abcm_kyc_reporting_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatusCheckRequest {

    @JsonProperty("merchantId")
    private String merchantId;

    @JsonProperty("orderId")
    private String orderId;
}
