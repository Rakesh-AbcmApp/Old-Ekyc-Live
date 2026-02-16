package com.abcm.esign_service.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel {
	
	private String message;        // Message for the response
    private int statusCode;        // Status code for the response
    private Object data;           // Data (could be a list, object, etc.)
   
    // Constructor to handle responses with or without pagination
    public ResponseModel(String message, int statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        
    }

    

}
