package com.abcm.kyc.service.ui.restcall;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.kyc.service.ui.dto.KycReportRequestModel;
import com.abcm.kyc.service.ui.dto.ReportModel;
import com.abcm.kyc.service.ui.service.KycReportService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/app/public/")
@RequiredArgsConstructor
@Slf4j
public class KycReportController {

    private final KycReportService kycReportService;
    
    @PostMapping("kyc/ui/Report")
    public ResponseEntity<ReportModel> fetchKycReport(@RequestBody KycReportRequestModel kycReportRequestModel,
                                                     HttpServletRequest request) throws IOException {
    	log.info("Kyc Report Request Body{} "+kycReportRequestModel);
    	
    	ReportModel response = kycReportService.fetchKycReportData(kycReportRequestModel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    
    @PostMapping("kyc/ui/Report/Merchant")
    public ResponseEntity<ReportModel> fetchKycReportMerchant(@RequestBody KycReportRequestModel kycReportRequestModel,
                                                     HttpServletRequest request) throws IOException {
    	log.info("Kyc Report Merchant Request Body{} "+kycReportRequestModel);
    	
    	ReportModel response = kycReportService.fetchKycReportDataMerchant(kycReportRequestModel);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
   
}
