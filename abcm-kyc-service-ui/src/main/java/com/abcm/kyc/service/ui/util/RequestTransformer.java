package com.abcm.kyc.service.ui.util;

import java.util.HashMap;
import java.util.Map;

public class RequestTransformer {

	public static class TransformedRequests {
		private Map<String, Object> cashfreeRequest;
		private Map<String, Object> zoopRequest;

		// Getters and Setters
		public Map<String, Object> getCashfreeRequest() {
			return cashfreeRequest;
		}

		public void setCashfreeRequest(Map<String, Object> cashfreeRequest) {
			this.cashfreeRequest = cashfreeRequest;
		}

		public Map<String, Object> getZoopRequest() {
			return zoopRequest;
		}

		public void setZoopRequest(Map<String, Object> zoopRequest) {
			this.zoopRequest = zoopRequest;
		}
	}

	public static TransformedRequests transformRequests(Map<String, Object> masterRequest) {
		TransformedRequests transformed = new TransformedRequests();

		// 1. Cashfree Transformation
		Map<String, Object> cashfreeData = new HashMap<>();
		cashfreeData.put("aadhaar_number", masterRequest.get("AadhaarNumber"));

		Map<String, Object> cashfreeRequest = new HashMap<>();
		cashfreeRequest.put("data", cashfreeData);
		transformed.setCashfreeRequest(cashfreeRequest);

		// 2. Zoop Transformation
		Map<String, Object> zoopData = new HashMap<>();
		zoopData.put("consent_text", "I hereby declare my consent agreement for fetching my information via ZOOP API.");
		zoopData.put("customer_aadhaar_number", masterRequest.get("AadhaarNumber"));
		zoopData.put("consent", masterRequest.get("consent"));

		
		Map<String, Object> zoopRequest = new HashMap<>();
		zoopRequest.put("data", zoopData);
		transformed.setZoopRequest(zoopRequest);

		return transformed;
	}

	public static void main(String[] args) {
		Map<String, Object> masterRequest = new HashMap<>();
		masterRequest.put("merchantId", "");
		masterRequest.put("AadhaarNumber", "655675523712");
		masterRequest.put("consent", "Y");

		TransformedRequests transformed = transformRequests(masterRequest);
		System.out.println("Cashfree Request: " + transformed.getCashfreeRequest());
		System.out.println("Zoop Request: " + transformed.getZoopRequest());
	}
}