package com.abcm.kyc.service.ui.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.abcm.kyc.service.ui.ApiCall.KycReportApiClient;
import com.abcm.kyc.service.ui.dto.KycReportRequestModel;
import com.abcm.kyc.service.ui.dto.ReportModel;
import com.abcm.kyc.service.ui.repository.MerchantRepository;
import com.abcm.kyc.service.ui.service.KycReportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycReportServiceImpl implements KycReportService {

    private final KycReportApiClient apiClient;
    private final Environment env;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Single instance


    @Override
    public ReportModel fetchKycReportData(KycReportRequestModel requestModel) throws IOException {
        log.info("Inside Kyc Report Service Method: {}", requestModel);
        try {
            
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (requestModel.getFormDate() != null && !requestModel.getFormDate().trim().isEmpty()) {
                requestModel.setFormDate(LocalDate.parse(requestModel.getFormDate().trim(), inputFormatter).format(outputFormatter));
            }

            if (requestModel.getToDate() != null && !requestModel.getToDate().trim().isEmpty()) {
                requestModel.setToDate(LocalDate.parse(requestModel.getToDate().trim(), inputFormatter).format(outputFormatter));
            }

            if (requestModel.getProduct() != null && requestModel.getProduct().trim().isEmpty()) {
                requestModel.setProduct(null);
            }
            String apiResponse = apiClient.getOkycReport(
                requestModel,
                env.getProperty("urls.KYC_URL")
                
            );

           // log.info("API Response: {}", apiResponse);

            if (apiResponse == null || apiResponse.trim().isEmpty()) {
                return new ReportModel(500, "Empty response from API", null,null);
            }
            // Handle plain "failed" response
            if ("failed".equalsIgnoreCase(apiResponse.trim())) {
                return new ReportModel(404, "No records found", null,null);
            }
            // Parse response as JSON
            Map<String, Object> parsed = objectMapper.readValue(apiResponse, new TypeReference<>() {});
            Object data = parsed.get("data");
            Object pagination = parsed.get("paginationResponse");
            Object statusCode = parsed.get("statusCode");
            Object message = parsed.get("message");

            int finalStatus = (statusCode instanceof Integer) ? (Integer) statusCode : 200;
            String finalMessage = (message != null) ? message.toString() : "Success";

            return new ReportModel(finalStatus, finalMessage, data, pagination);

        } catch (Exception e) {
            log.error("Unexpected error for request {}: {}", requestModel, e.getMessage(), e);
            return new ReportModel(500, "Unexpected error", null,null);
        }
    }
    
    
    
    
    @Override
    public ReportModel fetchKycReportDataMerchant(KycReportRequestModel requestModel) throws IOException {
        log.info("Inside Kyc Report Service Method: {}", requestModel);
        try {
            
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (requestModel.getFormDate() != null && !requestModel.getFormDate().trim().isEmpty()) {
                requestModel.setFormDate(LocalDate.parse(requestModel.getFormDate().trim(), inputFormatter).format(outputFormatter));
            }

            if (requestModel.getToDate() != null && !requestModel.getToDate().trim().isEmpty()) {
                requestModel.setToDate(LocalDate.parse(requestModel.getToDate().trim(), inputFormatter).format(outputFormatter));
            }

            if (requestModel.getProduct() != null && requestModel.getProduct().trim().isEmpty()) {
                requestModel.setProduct(null);
            }
            String apiResponse = apiClient.getOkycReport(
                requestModel,
                env.getProperty("urls.KYC_Report_Merchnat")
                
            );

           // log.info("API Response: {}", apiResponse);

            if (apiResponse == null || apiResponse.trim().isEmpty()) {
                return new ReportModel(500, "Empty response from API", null,null);
            }
            // Handle plain "failed" response
            if ("failed".equalsIgnoreCase(apiResponse.trim())) {
                return new ReportModel(404, "No records found", null,null);
            }
            // Parse response as JSON
            Map<String, Object> parsed = objectMapper.readValue(apiResponse, new TypeReference<>() {});
            Object data = parsed.get("data");
            Object pagination = parsed.get("paginationResponse");
            Object statusCode = parsed.get("statusCode");
            Object message = parsed.get("message");

            int finalStatus = (statusCode instanceof Integer) ? (Integer) statusCode : 200;
            String finalMessage = (message != null) ? message.toString() : "Success";

            return new ReportModel(finalStatus, finalMessage, data, pagination);

        } catch (Exception e) {
            log.error("Unexpected error for request {}: {}", requestModel, e.getMessage(), e);
            return new ReportModel(500, "Unexpected error", null,null);
        }
    }




}
