package com.abcm.addhar_service.merchantReponseDto;




import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.ToString;


@lombok.Data

@ToString
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OtpSendReponseToMerchnat {
      
       
	public String response_code;
    public String response_message;
    public boolean success;
    public String request_id;
    public String track_id;
    public String merchant_id;
    public String response_timestamp;
    public String billable;
   // public String order_id;
       
        
}

