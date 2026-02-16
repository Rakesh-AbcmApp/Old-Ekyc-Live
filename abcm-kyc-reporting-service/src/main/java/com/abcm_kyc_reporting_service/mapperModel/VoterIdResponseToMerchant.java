package com.abcm_kyc_reporting_service.mapperModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoterIdResponseToMerchant {

	public String response_code;
	public String response_message;
	public String billable;
	public Boolean success;
	public String track_id;
	public String merchant_id;

	@JsonProperty("Reference_ID")
	public String referenceId;

	@JsonProperty("EPIC_Number")
	public String epicNumber;

	@JsonProperty("Name")
	public String name;

	@JsonProperty("Gender")
	public String gender;

	@JsonProperty("Age")
	public Integer age;

	@JsonProperty("Relation_Type")
	public String relationType;

	@JsonProperty("Relation_Name")
	public String relationName;

	@JsonProperty("Parliamentary_Constituency_Number")
	public Integer parliamentaryConstituencyNumber;

	@JsonProperty("Parliamentary_Constituency")
	public String parliamentaryConstituency;

	@JsonProperty("Assembly_Constituency_Number")
	public Integer assemblyConstituencyNumber;

	@JsonProperty("Assembly_Constituency")
	public String assemblyConstituency;

	@JsonProperty("Part_Number")
	public String partNumber;

	@JsonProperty("Part_Name")
	public String partName;

	@JsonProperty("Serial_Number")
	public String serialNumber;

	@JsonProperty("Polling_Station_Name")
	public String pollingStationName;

	@JsonProperty("Request_Timestamp")
	public String requestTimestamp;

	@JsonProperty("Response_Timestamp")
	public String responseTimestamp;

	@JsonProperty("status")
	public String status;

}
