package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	@Autowired
	CategoryService service;
	
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
	
	@PostMapping("/categories/check_unique")
//	public String checkUnique(@RequestParam("id") Integer id,@RequestParam("name") String name,
//			@RequestParam("alias") String alias) {
	public String checkUnique(Integer id,String name,String alias) {
		return service.checkUnique(id, name, alias);
	}
}
