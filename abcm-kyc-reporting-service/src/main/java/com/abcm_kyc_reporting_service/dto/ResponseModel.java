package com.abcm_kyc_reporting_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {
	
	private String message;        // Message for the response
    private int statusCode;        // Status code for the response
    private Object data;           // Data (could be a list, object, etc.)
    private PaginationResponse paginationResponse;  // Pagination details

    // Constructor to handle responses with or without pagination
    public ResponseModel(String message, int statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        this.paginationResponse = null;  // No pagination in this case
    }

    // Constructor for responses with pagination
    public ResponseModel(String message, int statusCode, Object data, PaginationResponse paginationResponse) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        this.paginationResponse = paginationResponse;
    }

}
