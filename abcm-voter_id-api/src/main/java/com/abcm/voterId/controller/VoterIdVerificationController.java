package com.abcm.voterId.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.voterId.DTO.ResponseModel;
import com.abcm.voterId.DTO.VoterIdRequestModel;
import com.abcm.voterId.service.VerifyVoterIdService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Slf4j
public class VoterIdVerificationController {

	private final VerifyVoterIdService service;

	@PostMapping("voterid/verify")
	public ResponseEntity<ResponseModel> VerifyVoterId(@RequestBody VoterIdRequestModel voterIdReuestModel,
			@RequestHeader("app-id") String appId, @RequestHeader("api-key") String apiKey) {
		log.info("Received Voter ID verification request: {}", voterIdReuestModel);
		ResponseModel responseModel = service.verifyVoterId(voterIdReuestModel, appId, apiKey);
		return ResponseEntity.status(responseModel.getStatusCode()).body(responseModel);

	}

}
