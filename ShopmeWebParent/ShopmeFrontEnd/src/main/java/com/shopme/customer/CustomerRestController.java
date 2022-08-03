package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired private CustomerService service;
	
	//@RequestParam binds the value in the url to the method in the parameter with the name email
	//@PostMapping("/customers/check_unique_email")
	//public String checkDuplicateEmail(@RequestParam("email") String email) {
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(String email) {
		
		//returns ok if email is unique and duplicated if email is not unique
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
	
}
