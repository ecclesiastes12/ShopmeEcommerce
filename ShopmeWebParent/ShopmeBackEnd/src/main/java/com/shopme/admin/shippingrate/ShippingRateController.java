package com.shopme.admin.shippingrate;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.User;


@Controller
@Transactional
public class ShippingRateController {

	@Autowired ShippingRateService service;
	
	
	//handler method that list rates by page	
//	  @GetMapping("/shipping_rates") 
//	  public String listAll(Model model) { 
//		  List<ShippingRate> listRates = service.listAll(); 
//		  model.addAttribute("listRates", listRates);
//	  
//		  return "shippingrate/shipping_rate"; 
//	  }
	 
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	}
	

	 @GetMapping("/shipping_rates/page/{pageNum}") 
	  public String listAll(@PagingAndSortingParam(listName = "shippingRates", moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
				@PathVariable(name ="pageNum") int pageNum) { 
		 service.listByPage(pageNum,helper);
	  
		  return "shippingrate/shipping_rates"; 
	  }
	  
	  //handler method that display shipping rate form
	  @GetMapping("/shipping_rates/new")
	  public String showShippingRateForm(Model model) {
		  //list all countries and states
		  List<Country> listCountries = service.listAllCountries();
		  ShippingRate  rates = new ShippingRate();
		  model.addAttribute("rate", rates);
		  model.addAttribute("pageTitle", "New Rate");
		  model.addAttribute("listCountries", listCountries);
		  return "shippingrate/shipping_rates_form";
	  }
	  
	  //handler method that creates new rate
	  @PostMapping("/shipping_rates/save")
		public String saveRate(ShippingRate rate, RedirectAttributes ra) {
			try {
				service.save(rate);
				ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
			} catch (ShippingRateAlreadyExistsException ex) {
				ra.addFlashAttribute("message", ex.getMessage());
			}
		
		  return "redirect:/shipping_rates";
	  }
	  
	  //handler method that show update shipping rate form
	  @GetMapping("/shipping_rates/edit/{id}")
	  public String editShippingRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		  
		  try {
			ShippingRate rates = service.getRateById(id);
			List<Country> countries = service.listAllCountries();
			model.addAttribute("rate", rates);
			model.addAttribute("listCountries", countries);
			model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");
			return "shippingrate/shipping_rate_form";
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		  return "redirect:/shipping_rates";
	  }
	  
	  //handler method that update codSupport status
	  @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	  public String statusCODSupport(@PathVariable(name = "supported") boolean supported,
			  @PathVariable(name = "id") Integer id, RedirectAttributes ra, Model model) {
		  
		  try {
				service.updateCODSupport(supported, id);
				ra.addFlashAttribute("message", "COD support for shipping rate ID " + id + " has been updated.");
			} catch (ShippingRateNotFoundException ex) {
				ra.addFlashAttribute("message", ex.getMessage());
			}			
		  
		  return "redirect:/shipping_rates";
	  }
	  
	  //handler method that delete shipping rate
	  @GetMapping("/shipping_rates/delete/{id}")
	  public String deleteShipRate(@PathVariable(name = "id") Integer id, RedirectAttributes ra){
		  try {
			service.deleteShipRate(id);
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted.");
		} catch (ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		  return "redirect:/shipping_rates";
	  }
}
