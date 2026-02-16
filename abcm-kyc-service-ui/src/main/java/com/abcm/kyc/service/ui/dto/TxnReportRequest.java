package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TxnReportRequest {

	public String merchantId;
	public String fromDate;
	public String toDate;
	
}
