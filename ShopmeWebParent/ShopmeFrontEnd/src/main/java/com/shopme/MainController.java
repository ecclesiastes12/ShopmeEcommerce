package com.shopme;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class MainController {

	@Autowired CategoryService categoryService;
	
	@GetMapping("")
	public String viewHomePage(Model model) {
		
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}
	
	//handler method for customer login page
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
