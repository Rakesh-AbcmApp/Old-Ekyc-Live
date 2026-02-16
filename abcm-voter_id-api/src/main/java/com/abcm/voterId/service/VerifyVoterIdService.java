package com.abcm.voterId.service;

import com.abcm.voterId.DTO.ResponseModel;
import com.abcm.voterId.DTO.VoterIdRequestModel;

public interface VerifyVoterIdService {
	
	
	public ResponseModel verifyVoterId(VoterIdRequestModel voterIdReuestModel , String appId , String apiKey);

}
