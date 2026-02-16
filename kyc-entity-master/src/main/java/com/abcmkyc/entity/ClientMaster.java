package com.abcmkyc.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "client_master_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor // Hibernate needs this constructor for entity instantiation
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = {"productRates"})
public class ClientMaster {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Optional if the field name matches
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "environment")
    private String environment;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "service_provider_name")
    private String serviceProviderName;

    @Column(name = "api_key")
    private String apiKey;

    
    @Column(name = "api_aadhar_url1")
    private String apiAadharUrl1;

    @Column(name = "api_aadhar_url2")
    private String apiAadharUrl2;
    
    
    @Column(name = "api_gst_url1")
    private String apiGstUrl1;
    @Column(name = "api_gst_url2")
    private String apiGstUrl2;
    @Column(name = "api_pan_url1")
    private String apiPanUrl1;
    
    @Column(name = "api_pan_url2")
    private String apiPanUrl2;
    
    @Column(name = "driving_ls_url")
    private String drivingLsUrl;
    
    @Column(name = "voter_id_url")
    private String voterIdUrl;
    
    @Column(name = "udf1")
    private String udf1;
    
    @Column(name = "udf2")
    private String udf2;
    
    @Column(name = "udf3")
    private String udf3;
    
    @Column(name = "udf4")
    private String udf4;
    @Column(name = "udf5")
    private String udf5;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status;
    
    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
        
    }
    
    
   
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<MerchantRouting> routings;
    
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProviderProductRate> productRates;
   

    
     
    
    
   

    
}
