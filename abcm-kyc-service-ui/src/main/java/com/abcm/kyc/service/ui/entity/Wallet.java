//package com.abcm.kyc.service.ui.entity;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import lombok.Data;
//
//@Data
//@Entity
//public class Wallet {
//
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "merchant_id", nullable = false)
//    private String merchantId;
//
//    @Column(name = "balance")
//    private long balance;
//    
//    @Column(name = "alert_balance", length = 150)
//	private long alertBalance;
//
//    @Column(name = "txn_date")
//    private LocalDateTime txnDate;
//    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<TransactionHistory> transactions = new ArrayList<>();
//
//
//	
//	
//}
