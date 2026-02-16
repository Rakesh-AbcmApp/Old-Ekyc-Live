package com.abcm.dl_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter@ToString
public class PanReportRequestModel {
	public String mid;
	public String formDate;
	public String toDate;
	public String product;
	private Integer page;  // page number, starts from 0
	private Integer size;
}
