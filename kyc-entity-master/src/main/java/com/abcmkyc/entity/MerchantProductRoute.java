package com.abcmkyc.entity;



import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name = "merchant_product_route")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MerchantProductRoute {



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_route_id")
	private Long productRouteId;




	@Column(name="product_id",length = 20)
	private Long product_id;


	@Column(name="product_name",length = 50)
	private String productName;



	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mm_id", nullable = false)
	@JsonIgnore
	private Merchant_Master merchant;
	
	
	@Column(name="mid",length = 50)
	private String mid;
	
	


	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	public Long getProductRouteId() {
		return productRouteId;
	}

	public void setProductRouteId(Long productRouteId) {
		this.productRouteId = productRouteId;
	}

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Merchant_Master getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant_Master merchant) {
		this.merchant = merchant;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}



	
	













}
