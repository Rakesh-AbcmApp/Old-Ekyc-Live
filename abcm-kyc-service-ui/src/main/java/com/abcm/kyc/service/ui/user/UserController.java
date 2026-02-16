package com.abcm.kyc.service.ui.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/user/")
public class UserController {
	
	
	
	@GetMapping("dashboard")
	public String userDashBoard()
	{
		return "User/UserDashboard";
	}

}
