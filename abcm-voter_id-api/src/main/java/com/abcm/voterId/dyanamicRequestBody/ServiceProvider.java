package com.abcm.voterId.dyanamicRequestBody;

import com.abcm.voterId.DTO.VoterIdRequestModel;

public interface ServiceProvider<S> {

	S buildRequest(VoterIdRequestModel request);
}
