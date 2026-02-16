package com.abcm.kyc.service.ui.dto;

import lombok.Data;

@Data
public class AddBalanceRequest {

	private String merchantId;
	private String amount;
	private String mode;
	private String txnId;
	private String merchantName;
	private String email;
	private String mobileNo;
	//new added
	private String country;
	private String state;
	private String city;
	private String pincode;
	private String username;

	
}
