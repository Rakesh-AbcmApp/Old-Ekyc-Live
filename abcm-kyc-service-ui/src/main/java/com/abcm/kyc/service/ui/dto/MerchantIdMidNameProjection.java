package com.abcm.kyc.service.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MerchantIdMidNameProjection {
	 private Long id;
	    private String mid;
	    private String name;
}
