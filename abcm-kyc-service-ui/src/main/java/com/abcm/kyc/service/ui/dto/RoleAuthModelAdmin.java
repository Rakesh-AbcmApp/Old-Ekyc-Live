package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleAuthModelAdmin {
	public String mid;
	public String id;
	public String username;
	public String columnName;
	public String value;
	public String dataType;
}
