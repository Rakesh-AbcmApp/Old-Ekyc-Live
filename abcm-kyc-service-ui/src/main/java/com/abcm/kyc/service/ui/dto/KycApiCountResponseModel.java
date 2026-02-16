package com.abcm.kyc.service.ui.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class KycApiCountResponseModel {
	   private String message;   // fixed typo
	    private int code;
	    private List<?> data;
}
