package com.abcm.voterId.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoterIdRequestModel {
	
	
	private String merchantId;
	private String epicNumber;
	private String consent;
	private String customerName;
	//private String orderId;
}
