package com.abcm.kyc.service.ui.dto;


import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Immutable
@Table(name = "aadhar_master")
public class AadharMasterView {



	@Id

	@Column(name = "aaddhar_id")
	private Long aaddharId;

	@Column(name = "aadhar_number", length = 12)
	private String aadharNumber;

	@Column(name = "otp", length = 6)
	private String otp;

	@Column(name = "request_id", length = 100)
	private String requestId;

	@Column(name = "url_request_at")
	private LocalDateTime urlRequestAt;

	@Column(name = "url_response_at")
	private LocalDateTime urlResponseAt;

	@Column(name = "request", columnDefinition = "TEXT")
	private String request;

	@Column(name = "response", columnDefinition = "TEXT")
	private String response;

	@Column(name = "merchnat_id", length = 100)
	private String merchnatId;

	@Column(name = "source", length = 50)
	private String source;

	@Column(name = "client_provider_name", length = 100)
	private String clientProviderName;

	@Column(name = "consent", length = 100)
	private String consent;

	@Column(name = "aadhaar_verify_request",  columnDefinition = "TEXT")
	private String aadhaarVerifyRequest;


	@Column(name = "aadhaar_verify_response",  columnDefinition = "TEXT")
	private String aadhaarVerifyResponse;

	@Column(name = "otp_verify", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean otpVerify;

	@Column(name = "billable", length = 20)
	private String billable;



	@Column(name = "reason_message", length = 150)
	private String reasonMessage;


	@Column(name = "response_message", length = 50)
	private String responseMessage;


	@Column(name = "response_code", length = 50)
	private String responseCode;



	@Column(name = "aaddhar_request_result", length = 150)
	private String aaddharRequestResult;


	@Column(name = "aaddhar_response_result", length = 999)
	private String aaddharResponseResult;

}
