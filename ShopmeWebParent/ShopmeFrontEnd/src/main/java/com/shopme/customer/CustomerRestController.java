package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired private CustomerService service;
	
	//@Param binds the value in the url to the method in the parameter with the name email
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(@Param("email") String email) {
		
		//returns ok if email is unique and duplicated if email is not unique
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
	
}
