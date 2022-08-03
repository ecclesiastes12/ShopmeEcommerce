package com.shopme.admin;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("") //path declared in the properties file(context-path)
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/login") 
	public String viewLoginPage() {
		//This line of code grabs the authenticated user or token (authentication object)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		//checks the authentication object 
		//direct user to login page if user is not authenticated or user is a guest user
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/"; //prevent login page from displaying again once the user is login
	}
}
