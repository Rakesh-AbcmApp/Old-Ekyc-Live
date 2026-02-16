package com.abcm.gst_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MerchantGstMasterRequest {
    public String merchantId;
    public String businessGstinNumber;
    public String consent;
    //public String orderId;
}
 