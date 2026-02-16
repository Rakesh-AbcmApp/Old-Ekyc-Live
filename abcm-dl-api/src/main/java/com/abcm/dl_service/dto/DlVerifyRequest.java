package com.abcm.dl_service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DlVerifyRequest {
	public String merchantId;
	public String customer_dl_number;
	public String customer_dob;
	public String Consent;
	//public String orderId;

}
