package com.abcm.kyc.service.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    private int statusCode;
    private String message;
    private Object data;
    private Object paginationResponse; // ✅ This holds the pagination block
}
