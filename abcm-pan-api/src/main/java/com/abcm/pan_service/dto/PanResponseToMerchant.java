package com.abcm.pan_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Data
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PanResponseToMerchant {
	public String response_code;
	public String response_message;
	public String billable;
	public Boolean success;
	public String track_id;
	public String merchant_id;
    public String reference_id;
	public String response_timestamp;
	private String pan_number;
	private String user_full_name;
	private String user_email;
	private String user_phone_number;
	private String  user_dob;
	private String user_gender;
	private String masked_aadhaar;
	private Boolean aadhaar_linked_status;
	private UserAddress  user_address;
	//private String order_id;
	
	@Data
	@Getter
	@Setter
	@ToString
	
	
	public static class  UserAddress
	{
		private String country;
		private String state;
		private String city;
		private String pincode;
		private String street;
		private String fullAddress;
		
		
		
		
	}
	




}
