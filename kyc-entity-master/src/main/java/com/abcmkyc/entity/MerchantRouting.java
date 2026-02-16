package com.abcmkyc.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;




@Entity
@Table(name = "merchant_routing")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MerchantRouting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "route_id")
	private Long routeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_id")

	private ClientMaster client;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductMaster product;



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_id")
	@JsonIgnore
	private Merchant_Master merchant;

	@Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isActive;


	@Column(name = "created_by")
	private String createdBy; 


	@Column(name = "created_at")
	private LocalDateTime createdAt;


	@Column(name = "updated_by")
	private String updatedBy; 


	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "mid",length = 20)
	private String mid;
	
	
	


	@OneToMany(mappedBy = "merchantRouting", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<MerchantCharges> charges = new ArrayList<>();





	public Long getRouteId() {
		return routeId;
	}





	public void setRouteId(Long routeId) {
		this.routeId = routeId;
	}





	public ClientMaster getClient() {
		return client;
	}





	public void setClient(ClientMaster client) {
		this.client = client;
	}





	public ProductMaster getProduct() {
		return product;
	}





	public void setProduct(ProductMaster product) {
		this.product = product;
	}





	public Merchant_Master getMerchant() {
		return merchant;
	}





	public void setMerchant(Merchant_Master merchant) {
		this.merchant = merchant;
	}





	public boolean isActive() {
		return isActive;
	}





	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}





	public String getCreatedBy() {
		return createdBy;
	}





	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}





	public LocalDateTime getCreatedAt() {
		return createdAt;
	}





	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}





	public String getUpdatedBy() {
		return updatedBy;
	}





	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}





	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}





	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}





	public String getMid() {
		return mid;
	}





	public void setMid(String mid) {
		this.mid = mid;
	}





	public List<MerchantCharges> getCharges() {
		return charges;
	}





	public void setCharges(List<MerchantCharges> charges) {
		this.charges = charges;
	}















}
