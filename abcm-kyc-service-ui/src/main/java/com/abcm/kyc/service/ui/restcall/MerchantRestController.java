package com.abcm.kyc.service.ui.restcall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.kyc.service.ui.dto.ApiResponseModel;
import com.abcm.kyc.service.ui.dto.ChangePasswordRequest;
import com.abcm.kyc.service.ui.service.MerchantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/app/public/")
@RequiredArgsConstructor
@Slf4j
public class MerchantRestController {
	
	
	@Autowired
	private MerchantService merchantService;
	
	
//	@PostMapping("add-balance")
//	public ResponseEntity<String> addBalance(@RequestBody AddBalanceRequest request) {
//		try {
//			walletService.addBalance(request.getMerchantId(), request.getAmount(), request.getMode(),
//					request.getTxnId());
//			return ResponseEntity.ok("Balance added successfully.");
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add balance: " + e.getMessage());
//		}
//	}

	@GetMapping("/kyc-data-count")
	public Map<String, Integer> getKycDataCount(@RequestParam String filterType,@RequestParam String merchantId) {
		log.info("kyc-data-count Controller Hit{}"+merchantId);
		int count = merchantService.getKycCount(merchantId, filterType);
		Map<String, Integer> response = new HashMap<>();
		response.put("count", count);
		return response;
	}

	@GetMapping("/kyc-data-monthwise")
	public List<Map<String, Object>> getKycDataMonthwise(
	        @RequestParam(value = "merchantId") String merchantId,
	        @RequestParam(value = "year") String year) {
	    

	    try {
	    	log.info("kyc-data-monthwise Controller Hit{}"+merchantId);
	        int yearInt = Integer.parseInt(year);
	        return merchantService.getProductUsageDataMonthwise(merchantId, yearInt);
	    } catch (NumberFormatException e) {
	      //  throw new IllegalArgumentException("Invalid year format: " + yearInt);
	    }
		return null;
	}


	
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
		
		log.info("Update Password method"+request);
	    boolean isChanged = merchantService.changePassword(
	        request.getMid(),
	        request.getNewPassword(),
	        request.getConfirmNewPassword()
	    );
	    if (isChanged) {
	        return ResponseEntity.ok(
	            new ApiResponseModel(200, "Password changed successfully.", null)
	        );
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	            new ApiResponseModel(400, "Merchant Not Found or password mismatch.", null)
	        );
	    }
	}

	

}
