package com.shopme.admin.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	
	@Autowired CustomerService service;
	
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		//same as if else statement 
		/*
		 * if(service.isEmailUnique(email)) { return "OK"; }else { return "Duplicated";
		 * }
		 */
		//return service.isEmailUnique(id,email) ? "OK" : "Duplicated";
		
		if (service.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
	

}
