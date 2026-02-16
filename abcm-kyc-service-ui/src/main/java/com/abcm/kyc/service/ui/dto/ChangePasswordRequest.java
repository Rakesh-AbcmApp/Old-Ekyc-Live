package com.abcm.kyc.service.ui.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	
	    private String mid;
	    private String newPassword;
	    private String confirmNewPassword;
}
