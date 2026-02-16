package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KycCountRequest {
	private String fromDate;
	private String toDate;
	private String merchantId;
	private String product;

}
