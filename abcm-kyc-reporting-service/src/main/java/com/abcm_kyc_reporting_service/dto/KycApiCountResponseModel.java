package com.abcm_kyc_reporting_service.dto;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KycApiCountResponseModel {

    private String message;   // fixed typo
    private int code;
    private List<?> data;
	public KycApiCountResponseModel(String message, int code, List<?> data) {
		super();
		this.message = message;
		this.code = code;
		this.data = data;
	}

    
}
