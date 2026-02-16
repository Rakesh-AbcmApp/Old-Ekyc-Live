//package com.abcm.addhar_service.entity;
//
//import java.time.LocalDateTime;
//
//import org.hibernate.annotations.Immutable;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@ToString
//@Entity
//@Immutable
//@Table(name = "wallet")
//public class Wallet {
//
//    @Id
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
//    private long alertBalance;
//
//    @Column(name = "txn_date")
//    private LocalDateTime txnDate;
//
//    // No setters = read-only
//}