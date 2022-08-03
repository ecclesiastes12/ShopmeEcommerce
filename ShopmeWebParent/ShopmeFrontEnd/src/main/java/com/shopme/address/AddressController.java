package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

@Controller
public class AddressController {

	@Autowired AddressService addressService;
	@Autowired CustomerService customerService;
	
	
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		//get authenticated customer
		Customer customer = getAuthenticatedCustomer(request);
		
		List<Address> listAddresses = addressService.listAddressBook(customer);
		
		//use primary address as default address
		boolean usePrimaryAddressAsDefault = true;
		
		//iterate through the address in the list of addresses
		for(Address address : listAddresses) {
			//check if address is already set as default address for shipping
			if(address.isDefaultForShipping()) {
				//if true then set usePrimaryAddressAsDefault to false
				usePrimaryAddressAsDefault = false;
				break; //ends the loop
			}
		}
		model.addAttribute("listAddresses", listAddresses);
		
		//for the purpose of display the customer address as the primary address
		model.addAttribute("customer", customer);
		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		
		return "address_book/addresses";
	}
	
	//handler method that return customer object of the authenticated customer
	private Customer getAuthenticatedCustomer(HttpServletRequest request){
		//get email of the authenticated customer
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		//NB no exception is thrown here because customer must login before s/he can view address book
		
		return customerService.getCustomerByEmail(email);
	}
	
	//handler method that show address form
	@GetMapping("/address_book/new")
	public String showAddressForm(Model model) {
		
		List<Country> listCountries = customerService.listAllCountries();
		
		
		Address address = new Address();
		model.addAttribute("address", address);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Add New Address");
		return "address_book/address_form";
	}
	
//	//handler method that save address
//	@PostMapping("/address_book/save")
//	public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
//		//get authenticated customer
//		Customer customer = getAuthenticatedCustomer(request);
//	
//		//set customer id
//		address.setCustomer(customer);
//		
//		addressService.saveAddress(address);
//		
//		ra.addFlashAttribute("message", "The address for customer " + customer.getFullName() + " has been saved");
//		return "redirect:/address_book";
//	}

	//method modified for editing address from the checkout page that is when redirect=checkout
	//handler method that save address
		@PostMapping("/address_book/save")
		public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) {
			//get authenticated customer
			Customer customer = getAuthenticatedCustomer(request);
		
			//set customer id
			address.setCustomer(customer);
			
			addressService.saveAddress(address);
			
			//creates parameter request for redirecting the page
			String redirectOption = request.getParameter("redirect");
			
			//redirect to account details page if customer update the account from the accounts page
			String redirectURL = "redirect:/address_book";	
			
			//checks if customer is updating or changing address from the checkout page 
			if("checkout".equals(redirectOption)) { 
				//append the parameter to the url
				redirectURL += "?redirect=checkout";
			}

			
			ra.addFlashAttribute("message", "The address for customer " + customer.getFullName() + " has been saved");
			//return "redirect:/address_book";
			
			return redirectURL;
		}
		
	
	
	//handler method that update address book
	@GetMapping("/address_book/edit/{id}")
	public String updateAddress(@PathVariable(name = "id") Integer addressId, Integer customerId,
			Model model, HttpServletRequest request) {
		
		List<Country> listCountries = customerService.listAllCountries();
		
		//get authenticated customer
		Customer customer = getAuthenticatedCustomer(request);
		
		Address address = addressService.getAddress(addressId, customer.getId());		
		model.addAttribute("address", address);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");
		
		return "address_book/address_form";
	}
	
	//handler method that delete address
	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable(name = "id") Integer addressId,
			Model model, HttpServletRequest request, RedirectAttributes ra) {
		//get authenticated customer
		Customer customer = getAuthenticatedCustomer(request);
				
		
		addressService.deleteAddress(addressId, customer.getId());
		ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted");
		
		return "redirect:/address_book";
	}
	
	//handler method for setting default address
//	@GetMapping("/address_book/default/{id}")
//	public String setDefaultAddresss(@PathVariable(name = "id") Integer addressId,
//			HttpServletRequest request) {
//		
//		//get authenticated customer object from the object request
//		Customer customer = getAuthenticatedCustomer(request);
//		
//		//set default customer address
//		addressService.setDefaultAddress(addressId, customer.getId());
//		
//		//creates parameter request for redirecting the page
//				String redirectOption = request.getParameter("redirect");
//		 		
//				//redirect to account details page if customer update the account from the accounts page
//				String redirectURL = "redirect:/address_book";			
//				
//				//checks if customer is updating or changing address from the cart page
//				if("cart".equals(redirectOption)) {
//					//redirect the page back to cart page if the update or change of address in the address book is done from the cart page
//					redirectURL = "redirect:/cart";
//				}
//		
//		return redirectURL;
//	}
	
	//method modified
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddresss(@PathVariable(name = "id") Integer addressId,
			HttpServletRequest request) {
		
		//get authenticated customer object from the object request
		Customer customer = getAuthenticatedCustomer(request);
		
		//set default customer address
		addressService.setDefaultAddress(addressId, customer.getId());
		
		//creates parameter request for redirecting the page
		String redirectOption = request.getParameter("redirect");
		
		//redirect to account details page if customer update the account from the accounts page
		String redirectURL = "redirect:/address_book";	
		
		//checks if customer is updating or changing address from the cart page 
		if("cart".equals(redirectOption)) {
			
			//redirect the page back to cart page if the update or change of address in the address book is done from the cart page
			redirectURL = "redirect:/cart";
			
			//checks if customer is updating or changing address from the checkout page 
		}else if("checkout".equals(redirectOption)) { 
			redirectURL = "redirect:/checkout";
		}
		
		return redirectURL;
	}
}
