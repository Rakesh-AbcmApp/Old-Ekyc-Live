package com.abcmkyc.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity(name = "merchant_charges")
@Data
@Getter
@Setter
public class MerchantCharges {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "merchant_charge_id")
	private long merchantChargeId;
	
	@Column(name="product_rate")
	
	private Long productRate;
	
	@Column(name="merchant_id")
	private String merchantId;



	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
	 @JsonIgnore
    private MerchantRouting merchantRouting;



}
