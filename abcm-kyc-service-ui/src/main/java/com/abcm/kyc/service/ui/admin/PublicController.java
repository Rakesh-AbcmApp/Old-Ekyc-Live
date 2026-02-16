package com.abcm.kyc.service.ui.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.abcm.kyc.service.ui.dto.PaymentResponseModel;
import com.abcm.kyc.service.ui.payment.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller

public class PublicController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/payment-response")
	public String handlePaymentResponse(HttpServletRequest request,
	                                    RedirectAttributes redirectAttributes,
	                                    HttpSession session) {
	    PaymentResponseModel paymentResponse = paymentService.handleWalletBalanceUpdate(request);
	    session.setAttribute("paymentResponse", paymentResponse);
	    return "redirect:/app/merchant/billing";
	}

	


}
