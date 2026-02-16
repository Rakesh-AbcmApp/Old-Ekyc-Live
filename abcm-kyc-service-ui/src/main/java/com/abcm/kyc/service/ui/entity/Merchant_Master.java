//package com.abcm.kyc.service.ui.entity;
//
//import com.abcm.kyc.service.ui.Enum.AadharOkyc;
//import com.abcm.kyc.service.ui.Enum.Apps;
//import com.abcm.kyc.service.ui.Enum.Billing;
//import com.abcm.kyc.service.ui.Enum.Dashboard;
//import com.abcm.kyc.service.ui.Enum.GstLit;
//import com.abcm.kyc.service.ui.Enum.KycReport;
//import com.abcm.kyc.service.ui.Enum.PanPro;
//import com.abcm.kyc.service.ui.Enum.Product;
//import jakarta.persistence.Column;
//import jakarta.persistence.Embedded;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//import lombok.ToString;
//
//@Data
//@Table(name = "merchant_master")
//@Entity
//@ToString
//public class Merchant_Master {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id")
//	private long id;
//
//	@Column(name = "mid")
//	private String mid;
//
//	@Column(name = "name", length = 50)
//	private String name;
//
//	/* new Column */
//	@Column(name = "source", length = 50)
//	private String source;
//
//	@Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
//	private boolean status;
//
//	@Column(name = "mode", length = 50)
//	private String mode;
//
//	@Column(name = "rate", length = 50)
//	private long rate;
//
//	@Column(name = "email", length = 100)
//	private String email;
//
//	@Column(name = "service_providername", length = 100)
//	private String serviceProviderName;
//
//	@Column(name = "website_url", length = 100)
//	private String websiteUrl;
//
//	@Column(name = "buissness_aaddress", length = 100)
//	private String buissnessAaddress;
//
//	@Column(name = "country", length = 100)
//	private String country;
//
//	@Column(name = "state", length = 100)
//	private String state;
//
//	@Column(name = "city", length = 100)
//	private String city;
//
//	@Column(name = "pincode", length = 100)
//	private String pincode;
//
//	@Column(name = "phone_one", length = 100)
//	private String phoneOne;
//
//	@Column(name = "phone_two", length = 100)
//	private String phoneTwo;
//
//	@Column(name = "business_type", length = 100)
//	private String businessType;
//
//	@Column(name = "aaddhar_rate",  columnDefinition = "double DEFAULT 0.0",length = 10)
//	private double AaddharRate;
//
//	@Column(name = "pan_rate", length = 10, columnDefinition = "double DEFAULT 0.0")
//	private double PanRate;
//
//	@Column(name = "gst_rate", length = 10, columnDefinition = "double DEFAULT 0.0")
//	private double GstRate;
//
//	@Column(name = "gstadvance_rate", length = 10, columnDefinition = "double DEFAULT 0.0")
//	private double gstAdvanceRate;
//
//	@Embedded
//	private Credentials credentials;
//
//	@ManyToOne
//	private Role role;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private Dashboard dashboard = Dashboard.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private Billing billing = Billing.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private Product product = Product.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private Apps apps = Apps.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private AadharOkyc aadharOkyc = AadharOkyc.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private PanPro panPro = PanPro.DESABLE;
//
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private GstLit gstLit = GstLit.DESABLE;
//	
//	@Column(length = 8, columnDefinition = "varchar(10) default 'DESABLE'")
//	@Enumerated(EnumType.STRING)
//	private KycReport kycReport = KycReport.DESABLE;
//
//	@ManyToOne
//	ClientMaster clientMaster;
//	
//	@Column(name = "api_key", length = 100)
//	private String apiKey;
//	
//	@Column(name = "app_id", length = 100)
//	private String appId;
//	
//
//}
