package com.abcm.kyc.service.ui.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class TxnReportDto {
    private final String merchantId;
    private final String merchantName;
    private final String txnId;
    private final Long   txnAmount;
    private final LocalDateTime txnDate;
    private final String txnMode;
    private final String orderId;
    private final String status;

    public TxnReportDto(String merchantId,
                        String merchantName,
                        String txnId,
                        Long   txnAmount,
                        LocalDateTime txnDate,
                        String txnMode,
                        String orderId,
                        String status) {
        this.merchantId   = merchantId;
        this.merchantName = merchantName;
        this.txnId        = txnId;
        this.txnAmount    = txnAmount;
        this.txnDate      = txnDate;
        this.txnMode      = txnMode;
        this.orderId      = orderId;
        this.status       = status;
    }
}