package com.abcm_kyc_reporting_service.mapperModel;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor // ✅ This is what Jackson needs
@AllArgsConstructor // ✅ Also needed if using @Builder
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

}
