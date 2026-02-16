package com.abcm.kyc.service.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OnboardClientRequest {

	private long id;
	private String ProviderName;
	private String AaddharUrl1;
	private String AaddharUrl2;
	private String GstUrl1;
	private String PanUrl1;
	private String drivingLicense;
	private String voterId;
	private String Udf1;
	private String Udf2;
	private String Udf3;
	private String Udf4;
	private String Udf5;
	private String apiId;
	private String apiKey;
	private String environment;

}
