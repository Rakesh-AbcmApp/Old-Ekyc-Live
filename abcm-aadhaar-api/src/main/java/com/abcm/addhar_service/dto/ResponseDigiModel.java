package com.abcm.addhar_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDigiModel {
	
	 private boolean success;
	    private String sdkUrl;
	    private int statusCode;
	    private String message;
	    private String data;

}
