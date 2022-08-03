//package com.shopme.checkout;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.shopme.Utility;
//import com.shopme.address.AddressService;
//import com.shopme.common.entity.Address;
//import com.shopme.common.entity.CartItem;
//import com.shopme.common.entity.Customer;
//import com.shopme.common.entity.ShippingRate;
//import com.shopme.common.entity.order.PaymentMethod;
//import com.shopme.customer.CustomerService;
//import com.shopme.order.OrderService;
//import com.shopme.shipping.ShippingRateService;
//import com.shopme.shoppingcart.ShoppingCartService;
//
//@Controller
//public class CheckoutController1 {
//
//	@Autowired CheckoutService checkoutService;
//	@Autowired CustomerService customerService;
//	@Autowired AddressService addressService;
//	@Autowired ShippingRateService shipService;
//	@Autowired ShoppingCartService cartService;
//	@Autowired OrderService orderService;
//	
//	
//	//handler method that show checkout page
//	@GetMapping("/checkout")
//	public String showCheckoutPage(Model model, HttpServletRequest request) {
//		
//		//method call
//		Customer  customer = getAuthenticatedCustomer(request);
//		
//		//gets the default address of the customer 
//		Address defaultAddress = addressService.getDefaultAddress(customer);
//		ShippingRate shippingRate = null;
//		
//		//commented out because of defaultAddress.toString() and customer.toString() in the modal
//		//boolean usePrimaryAddressAsDefault = false;
//		
//		//checks if defaultAddress is not null that is if address in the customer
//		//address book is used as a shipping address
//		if(defaultAddress != null) {
//			//address in the address book will be displayed in the checkout page
//			model.addAttribute("shippingAddress", defaultAddress.toString());
//			
//			//gets the shipping rate of the default address
//			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
//		}else {
//			
//			//customer primary address is the address to be displayed in the checkout page
//			model.addAttribute("shippingAddress", customer.toString());
//			//use primary address as default in case default address is null
//			//commented out because of defaultAddress.toString() and customer.toString() in the modal
//			//usePrimaryAddressAsDefault = true;
//			
//			//uses customer address as default address if none of the address in the 
//			//address book is used as shipping address
//			shippingRate = shipService.getShippingRateForCustomer(customer);
//		}
//		
//		//redirect the customer to shopping cart if shipping rate is null
//		if(shippingRate == null) {
//			return "redirect:/cart";
//		}
//		
//
//		//list cart item based on a customer
//		List<CartItem> cartItems = cartService.listCartItems(customer);
//		
//		//prepare checkout info from checkout service
//		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
//		
//		//add checkout info for display
//		model.addAttribute("checkoutInfo", checkoutInfo);
//		
//		//add cart items products for display
//		model.addAttribute("cartItems", cartItems);
//
//		return "checkout/checkout";
//	}
//	
//	// method that return customer object of the authenticated customer
//	private Customer getAuthenticatedCustomer(HttpServletRequest request){
//		//get email of the authenticated customer
//		String email = Utility.getEmailOfAuthenticatedCustomer(request);
//		
//		//NB no exception is thrown here because customer must login before s/he can view cart items
//		
//		return customerService.getCustomerByEmail(email);
//	}
//
//	//handler method for placing an order with COD
//	@PostMapping("/place_order")
//	public String placeOrder(HttpServletRequest request) {
//		
//		//get the payment type. NB paymentMethod is the name attribute value "paymentMethod" 
//		//of the radio button cash on delivery in checkout page
//		String paymentType = request.getParameter("paymentMethod");
//		
//		//create new payment method
//		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
//		
//		//method call
//		Customer  customer = getAuthenticatedCustomer(request);
//		
//		//gets the default address of the customer 
//		Address defaultAddress = addressService.getDefaultAddress(customer);
//		ShippingRate shippingRate = null;
//		
//		//commented out because of defaultAddress.toString() and customer.toString() in the modal
//		//boolean usePrimaryAddressAsDefault = false;
//		
//		//checks if defaultAddress is not null that is if address in the customer
//		//address book is used as a shipping address
//		if(defaultAddress != null) {
//			//gets the shipping rate of the default address
//			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
//		}else {	
//			//uses customer address as default address if none of the address in the 
//			//address book is used as shipping address
//			shippingRate = shipService.getShippingRateForCustomer(customer);
//		}
//		
//		
//		//list cart item based on a customer
//		List<CartItem> cartItems = cartService.listCartItems(customer);
//		
//		//prepare checkout info from checkout service
//		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
//		
//		//creates order
//		orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
//		
//		//empty cart after order has been completed
//		cartService.deleteByCustomer(customer);
//		
//		return "/checkout/order_completed";
//	}
//	
//	
//}
