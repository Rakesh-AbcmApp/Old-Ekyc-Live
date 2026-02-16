package com.abcm.gst_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter@Setter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantToGstinResponse { 
	private String response_code;
	private String response_message;
	private Boolean success;
	private String referenceId;
	private String track_id;
	private String merchant_id;
	private String billable;
	private String response_timestamp;
	private String gstin;                      
	private String legal_name;         
	private String trade_name;         
	private String register_date;        
	private String business_constitution;      
	private String tax_payer_type;                
	private String last_updated;   
	private String state_jurisdiction;
	private String central_jurisdiction;
	private List<String> nature_of_business_activities;
	private List<OtherBusinessAddress> other_business_address;
	private primaryBusinessAddress primary_business_address;
	private String order_id;


    @Data
    @Getter
    @Setter
    public static class OtherBusinessAddress {
        private String pincode;
        private String state;
        private String district;
        private String location;
        private String building_number;
        private String building_name;
        private String full_address;
    }
	
	@Data
	@Getter
	@Setter
	public static class primaryBusinessAddress
	{
		
		private String building_name;
		private String building_number;
		private String city;
		private String district;
		private String flat_number;
		private String latitude;
		private String location;
		private  String longitude;
		private String business_nature;
		private String pincode;
		private String street;
		private String state_code;
		private String full_address;	
	}

	

}
