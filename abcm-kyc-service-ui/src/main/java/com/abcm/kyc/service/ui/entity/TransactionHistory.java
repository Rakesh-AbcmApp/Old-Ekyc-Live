//package com.abcm.kyc.service.ui.entity;
//
//import java.time.LocalDateTime;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Data;
//import lombok.ToString;
//
//@Data
//@Entity
//@ToString(exclude = "wallet")
//public class TransactionHistory {
//	
//	 @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;
//
//	    @Column(name = "txn_id")
//	    private String txnId;
//
//	    @ManyToOne(fetch = FetchType.LAZY)
//	    @JoinColumn(name = "wallet_id")
//	    @JsonIgnore
//
//	    private Wallet wallet;
//
//	    @Column(name = "mode")
//	    private String mode;
//
//	    @Column(name = "initiate_date")
//	    private LocalDateTime initiateDate;
//
//	    @Column(name = "payment_date")
//	    private LocalDateTime paymentDate;
//
//	    @Column(name = "request", columnDefinition = "json default null")
//	    private String request;
//
//	    @Column(name = "response", columnDefinition = "json default null")
//	    private String response;
//
//	    @Column(name = "amount")
//	    private Long amount;
//
//	    @Column(name = "txn_status")
//	    private String txnStatus;
//	    
//	    @Column(name = "order_id")
//	    private String orderId;
//	    
//	    @Column(name = "pay_url")
//	    private String payUrl;
//	    
//	   
//	    
//	    
//}
