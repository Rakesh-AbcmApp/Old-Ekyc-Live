package com.abcm.kyc.service.ui.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RoutingResponseContainer {

    public record RoutingDetail(
        @JsonProperty("clientId") Long clientId,
        @JsonProperty("providerName") String providerName,
        @JsonProperty("productId") Long productId,
        @JsonProperty("productName") String productName,
        @JsonProperty("rate") Long rate
    ) {}
    public record Data(
        @JsonProperty("routingDetails") List<RoutingDetail> routingDetails
    ) {}
    public record RoutingResponse(
        @JsonProperty("responseCode") int responseCode,
        @JsonProperty("massage") String message,
        @JsonProperty("data") Data data
    ) {}
}

