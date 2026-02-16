package com.abcm_kyc_reporting_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Merchant_Master {
	
	private long id;
    private String mid;
    private String name;
    private String source;
    private boolean status;
    private String mode;
    private long rate;
    private String email;
    private String serviceProviderName;
    private String websiteUrl;
    private String buissnessAaddress;
    private String country;
    private String state;
    private String city;
    private String pincode;
    private String phoneOne;
    private String phoneTwo;
    private String businessType;
    private double gstAdvanceRate;
    private double aaddharRate;
    private double panRate;
    private double gstRate;
    private String dashboard;
    private String billing;
    private String product;
    private String apps;
    private String aadharOkyc;
    private String panPro;
    private String gstLit;
    private String apiKey;
    private String appId;private ClientMaster clientMaster;

	@Data
	public static class ClientMaster
	{
		private int id;
	    private String createdBy;
	    private LocalDateTime createdOn;
	    private String environment;
	    private String appId;
	    private String serviceProviderName;
	    private String apiKey;
	    private String apiAadharUrl1;
	    private String apiAadharUrl2;
	    private String apiGstUrl1;
	    private String apiGstUrl2;
	    private String apiPanUrl1;
	    private String apiPanUrl2;
	    private String udf1;
	    private String udf2;
	    private String udf3;
	    private String udf4;
	    private String udf5;
	    private Boolean status;
		
	}
}