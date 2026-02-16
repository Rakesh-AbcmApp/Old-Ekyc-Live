package com.abcm_kyc_reporting_service.exception;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("response_code")
    private int responseCode;

    @JsonProperty("error")
    private String error;

    @JsonProperty("response_message")
    private String responseMessage;

    @JsonProperty("path")
    private String path;

    @JsonProperty("request_timestamp")
    private OffsetDateTime requestTimestamp;

}