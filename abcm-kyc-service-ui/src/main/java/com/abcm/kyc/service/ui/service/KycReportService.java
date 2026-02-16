package com.abcm.kyc.service.ui.service;

import java.io.IOException;

import com.abcm.kyc.service.ui.dto.KycReportRequestModel;
import com.abcm.kyc.service.ui.dto.ReportModel;

public interface KycReportService {
	
	
	
	public ReportModel fetchKycReportData(KycReportRequestModel kycReportRequestModel) throws IOException;
	
	
	public ReportModel fetchKycReportDataMerchant(KycReportRequestModel kycReportRequestModel) throws IOException;

}
