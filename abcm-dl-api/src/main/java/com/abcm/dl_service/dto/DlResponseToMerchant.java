package com.abcm.dl_service.dto;

import java.util.List;
import java.util.Map;

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
public class DlResponseToMerchant {
	public String response_code;
	public String response_message;
	public String billable;
	public Boolean success;
	public String track_id;
	public String merchant_id;
    public String reference_id;
	public String request_timestamp;
	public String response_timestamp;
	public String dl_number;
	public String status;
	public String dl_validity;
	public String date_of_issue;
	public String name;
	public String  father_or_husband_name;
	public String user_image;
	public String complete_address;
	public String district;
	public String state;
	public String city;
	public String pincode;
	public String country;
	public List<Map<String, String>> cov_details;
	//public String order_id;

}
