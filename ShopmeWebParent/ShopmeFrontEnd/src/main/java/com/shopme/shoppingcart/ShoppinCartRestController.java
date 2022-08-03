package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController

public class ShoppinCartRestController {

	@Autowired ShoppingCartService cartService;
	@Autowired CustomerService customerService;
	
	//handler method to process add product to cart
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity,
			HttpServletRequest request) {
		
		//method call
		//get email of the authenticated customer
		try {
			Customer customer = getAuthenticatedCustomer(request);
			
			//update cart item quantity if customer is already logged in
			Integer updateQuantity = cartService.addProduct(productId, quantity, customer);
			
			return updateQuantity + " item(s) of this product is were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart.";
		}catch (ShoppingCartException ex) {
			return ex.getMessage();
		}
		
		
	}
	
	
	//handler method that return customer object of the authenticated customer
	private Customer getAuthenticatedCustomer(HttpServletRequest request) 
			throws CustomerNotFoundException {
		//get email of the authenticated customer
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		//throws exception if customer email is not found
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated Customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	
	
	//handler method to process update product quantity in cart
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity,
			HttpServletRequest request) {	
		//method call
		//get email of the authenticated customer
		try {
			Customer customer = getAuthenticatedCustomer(request);
			
			//update cart item subtotal
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			//return string value of subtotal
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to change quantity of product.";
		}
	}
	
	//handler method that remove product from cart
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) {
		try {
			//get authenticated cstomer object through HttpServletRequest
			Customer customer = getAuthenticatedCustomer(request);
			
			//remove product
			cartService.removeProduct(productId, customer);
			
			return "The product has been removed from your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
		
	}
	
}
