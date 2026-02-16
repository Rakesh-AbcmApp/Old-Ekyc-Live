package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdatePasswordModel {
	
	
	 private String username;
	    private String newPassword;
	    private String confirmNewPassword;

}
