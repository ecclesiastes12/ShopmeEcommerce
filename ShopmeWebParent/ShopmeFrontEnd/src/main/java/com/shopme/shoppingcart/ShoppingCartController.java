package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class ShoppingCartController {

	@Autowired ShoppingCartService cartService;
	@Autowired CustomerService customerService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		//method call
		Customer  customer = getAuthenticatedCustomer(request);
		
		//list cart item based on a customer
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		model.addAttribute("cartItems", cartItems);
		
		return "cart/shopping_cart";
	}
	
	
	//handler method that return customer object of the authenticated customer
		private Customer getAuthenticatedCustomer(HttpServletRequest request){
			//get email of the authenticated customer
			String email = Utility.getEmailOfAuthenticatedCustomer(request);
			
			//NB no exception is thrown here because customer must login before s/he can view cart items
			
			return customerService.getCustomerByEmail(email);
		}
}
