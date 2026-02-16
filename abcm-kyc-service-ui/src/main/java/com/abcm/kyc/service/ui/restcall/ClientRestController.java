package com.abcm.kyc.service.ui.restcall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.kyc.service.ui.dto.ApiResponseModel;
import com.abcm.kyc.service.ui.dto.OnboardClientRequest;
import com.abcm.kyc.service.ui.service.OnboardClientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/public/")
@RequiredArgsConstructor
public class ClientRestController {
	
	
	private final OnboardClientService onboardClientService;
	
	
	@PostMapping("onboardClient")
	public ResponseEntity<ApiResponseModel> clientOnboard(@RequestBody OnboardClientRequest clientRequest) {
	    return ResponseEntity.ok(onboardClientService.onboardClient(clientRequest));
	}
	
	
	
	@GetMapping("clientReport")
	public ResponseEntity<ApiResponseModel> clientOnboardReport() {
	    return ResponseEntity.ok(onboardClientService.onboardClientReport());
	}


}
