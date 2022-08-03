package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {

	@Autowired
	UserService service;
	
	/*
	 * NB The use of @RequestParam is optional if and only if the name of variable in the arguement
	 * is the same as that of the parameters in the request. eg public String checkDuplicateEmail(Integer id, String email){...}
	 * 
	 *  Use @RequestParam when you want to set default value or 
	 *  when parameter is not required or
	 *  when parameter name is different from arguement name
	 *  eg 
	 *  @GetMapping("/url")
	 *  public String handlerMethod(@RequestParam(name = "name", required = false), String productName){
	 *    //..
	 *  }
	 *  
	 */
	
	@PostMapping("/users/check_email")
	//public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email) {
	public String checkDuplicateEmail(Integer id, String email) {
		//same as if else statement 
		/*
		 * if(service.isEmailUnique(email)) { return "OK"; }else { return "Duplicated";
		 * }
		 */
		return service.isEmailUnique(id,email) ? "OK" : "Duplicated";
	}
}
