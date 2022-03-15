package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

import net.bytebuddy.asm.Advice.Return;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	//NB check CustomerController1 for the previous code
	@GetMapping("/customers")
	public String listByFirstPage(Model model) {
		//return listByPage(1,model,"firstName","asc", null);
		
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	}

//	@GetMapping("/customers/page/{pageNum}")
//	public String listByPage(
//			@PagingAndSortingParam(listName="listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
//			@PathVariable(name ="pageNum") int pageNum ,Model model,
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword) {
//		
//		
//		//Page<Customer> page = customerService.listByPage(pageNum,sortField,sortDir,keyword);
//		
//		//code modified with page object removed and sortField, sortDir and keyword parameters replaced with PagingAndSortingHelper helper
//		customerService.listByPage(pageNum,helper);
//				
//		//code similar to code in updateModelAttributes in PagingAndSortingArgumentHelper class
////		//get the content to page
////		List<Customer> listCustomers = page.getContent();
////		
////		// page counter
////		long startCount = (pageNum - 1) * CustomerService.CUSTOMER_PER_PAGE + 1;
////		long endCount = startCount + CustomerService.CUSTOMER_PER_PAGE - 1;
////		
////		//gets the last page number
////		if(endCount > page.getTotalElements()) {
////			endCount = page.getTotalElements();
////		}
////		
////		//reverse sorting
////		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
////				
////		//set category id if it is not null
////		//if(categoryId != null) model.addAttribute("categoryId", categoryId);
////		
////		//model.addAttribute("listProducts", listProducts);
////		model.addAttribute("listCustomers", listCustomers);
////		
////		model.addAttribute("currentPage", pageNum);
////		model.addAttribute("totalPages", page.getTotalPages());
////		model.addAttribute("totalItems", page.getTotalElements());
////		model.addAttribute("startCount", startCount);
////		model.addAttribute("endCount", endCount);
////		
////		model.addAttribute("sortField", sortField);
////		model.addAttribute("sortDir", sortDir);
////		model.addAttribute("reverseSortDir", reverseSortDir);
////		model.addAttribute("keyword", keyword); //display the keyword
////		
////		//for url, this attribute will replace some of the moduleURL in the fragments.html
////		model.addAttribute("moduleURL", "/customers");	
//		
//		//method call
//		//this method can be called right in UserService class because listByPage
//		//method in UserService class parameters is modified. Therefore method is
//		//now moved to UserService class
////		helper.updateModelAttributes(pageNum, page);
//		
//		return "customers/customers";
//	}
	
//	@GetMapping("/customers/page/{pageNum}")
//	public String listByPage(
//			@PagingAndSortingParam(listName="listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
//			@PathVariable(name ="pageNum") int pageNum ,Model model,
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword) {
//		
//		
//		//Page<Customer> page = customerService.listByPage(pageNum,sortField,sortDir,keyword);
//		
//		//code modified with page object removed and sortField, sortDir and keyword parameters replaced with PagingAndSortingHelper helper
//		customerService.listByPage(pageNum,helper);				
//		
//		//method call
//		helper.updateModelAttributes(pageNum, page);
//		
//		return "customers/customers";
//	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName="listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable(name ="pageNum") int pageNum) {
		
		
		//Page<Customer> page = customerService.listByPage(pageNum,sortField,sortDir,keyword);
		
		//code modified with page object removed and sortField, sortDir and keyword parameters replaced with PagingAndSortingHelper helper
		customerService.listByPage(pageNum,helper);
		
		//method call
		//this method can be called right in CustomerService class because listByPage
		//method in UserService class parameters is modified. Therefore method is
		//now moved to CustomerService class
		//helper.updateModelAttributes(pageNum, page);
		
		return "customers/customers";
	}
	
	
	//handler method to enabled and disabled customer
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerStatus(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled, RedirectAttributes ra) {
		
		customerService.updateCustomerStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disabled";
		
		String message = "The customer ID " + id + " has been " + status;
		ra.addFlashAttribute("message", message);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			//grab the customer id
			Customer customer = customerService.getCustomerId(id);
			
			model.addAttribute("customer", customer);
			
			List<Country> countries = customerService.listAllCountries();
			model.addAttribute("listCountries", countries);
			//model.addAttribute("pageTitle", "Edit Customer ( ID: " + id + ")");
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));
			
			return "customers/customer_form";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "customers/customers";
		}
	}
	
	//handler method that save customers
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer,Model model, RedirectAttributes ra) {
		
		customerService.save(customer);
		ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been updated successfully.");
		return "redirect:/customers";
	}
	
	//handler method to show customer details
	@GetMapping("/customers/detail/{id}")
	public String viewCustomerDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			//grab the customer id
			Customer customer = customerService.getCustomerId(id);
			model.addAttribute("customer", customer);
			return "customers/customer_detail_modal";
			
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}
}
