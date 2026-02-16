package com.kyc_routing_service.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EkycRouteServiceTestController {

	@GetMapping("route/service/status")
	public String routeServiceTest()
	{
		return "e-kyc route service is up";
	}
}
