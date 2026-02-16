package com.abcm.addhar_service.merchantReponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DigiWebhookReponseToMerchnat {

	public String response_code;
	public String response_message;
	public boolean success;
	public String referenceId;
	public String track_id;
	public String merchant_id;
	
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	public String response_timestamp;
	private String billable;
	private String userFullName;
	private String userParentName;
	private String userAadhaarNumber;
	private String userDob;  
	private String userGender;
	private String house;
	private String street;
	private String landmark;
	private String loc;
	private String po;
	private String dist;
	private String subdist;
	private String vtc;
	private String state;
	private String country;
	private String addressZip;
	private String userProfileImage;
	private String order_id;

}
