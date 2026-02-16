package com.abcm.addhar_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AaddharReportRequestModel {
	
	
	
	public String mid;
	public String formDate;
	public String toDate;
	public String product;
	private Integer page;  // page number, starts from 0
	private Integer size;

}
