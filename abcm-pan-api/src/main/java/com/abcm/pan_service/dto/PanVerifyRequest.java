package com.abcm.pan_service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PanVerifyRequest {
	public String merchantId;
	public String customerPanNumber;
	public String consent;
	//public String orderId;

}
