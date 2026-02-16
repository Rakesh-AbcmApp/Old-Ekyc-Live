package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflineWalletRequest {

	public String mid;
	public String walletAmount;
	public String paymentMode;
	public String username;
}
