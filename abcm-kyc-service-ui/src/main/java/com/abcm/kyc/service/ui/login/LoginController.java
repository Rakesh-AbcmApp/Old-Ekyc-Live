package com.abcm.kyc.service.ui.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abcm.kyc.service.ui.service.MerchantService;

@Controller
public class LoginController {
	
	@Autowired
	MerchantService merchantService;

	@GetMapping({"/login", "/"})
	public String showMerchantLoginPage(
	        @RequestParam(value = "session", required = false) String session,
	        @RequestParam(value = "error", required = false) String error,
	        Model model) {
		int threadPoolSize = Runtime.getRuntime().availableProcessors();
		System.out.println("Thread PoolSize"+threadPoolSize);

	    if ("expired".equals(session)) {
	        //model.addAttribute("sessionMessage", "Session expired. Please login again.");
	    } else if ("invalid".equals(error)) {
	        model.addAttribute("errorMessage", "Invalid username or password.");
	    } 
	    return "Kyc-login";
	}

    
    
    
   @GetMapping("/getRole")
   @ResponseBody
   public String GetRole(@RequestParam(value = "username")String username)
   {
	   String response=merchantService.getRoleNameByUsername(username);
	   return response;
	   
   }
    
    
 
    
   
}
