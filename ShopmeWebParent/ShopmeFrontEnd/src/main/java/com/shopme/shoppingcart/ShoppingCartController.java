package com.shopme.shoppingcart;

import java.util.List;

import javax.mail.FetchProfile.Item;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {

	@Autowired ShoppingCartService cartService;
	@Autowired CustomerService customerService;
	@Autowired AddressService addressService;
	@Autowired ShippingRateService shipService;
	
//	@GetMapping("/cart")
// 	public String viewCart(Model model, HttpServletRequest request) {
//		//method call
//		Customer  customer = getAuthenticatedCustomer(request);
//		
//		//list cart item based on a customer
//		List<CartItem> cartItems = cartService.listCartItems(customer);
//		
//		float estimatedTotal = 0.0F;
//		//for estimated total
//		//iterate through each cart item
//		for (CartItem item : cartItems) {
//			//calculate estimated total
//			estimatedTotal += item.getSubtotal();
//		}
//		
//		model.addAttribute("cartItems", cartItems);
//		model.addAttribute("estimatedTotal", estimatedTotal);
//		
//		return "cart/shopping_cart";
//	}
//	
	
	//method modified with address and shipping rate
	@GetMapping("/cart")
 	public String viewCart(Model model, HttpServletRequest request) {
		//method call
		Customer  customer = getAuthenticatedCustomer(request);
		
		//list cart item based on a customer
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		//for estimated total
		//iterate through each cart item
		for (CartItem item : cartItems) {
			//calculate estimated total
			estimatedTotal += item.getSubtotal();
		}
		
		//gets the default address of the customer 
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;
		
		//checks if defaultAddress is not null that is if address in the customer
		//address book is used as a shipping address
		if(defaultAddress != null) {
		
			//gets the shipping rate of the default address
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		}else {
			//use primary address as default in case default address is null
			usePrimaryAddressAsDefault = true;
			
			//uses customer address as default address if none of the address in the 
			//address book is used as shipping address
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		
		//indicated shipping rate is supported or not
		model.addAttribute("shippingSupported", shippingRate != null);
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);
		
		return "cart/shopping_cart";
	}
	
	// method that return customer object of the authenticated customer
	private Customer getAuthenticatedCustomer(HttpServletRequest request){
		//get email of the authenticated customer
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		//NB no exception is thrown here because customer must login before s/he can view cart items
		
		return customerService.getCustomerByEmail(email);
	}



}
