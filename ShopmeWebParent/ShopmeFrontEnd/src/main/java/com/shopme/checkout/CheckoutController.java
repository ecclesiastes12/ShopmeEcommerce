package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {

	@Autowired CheckoutService checkoutService;
	@Autowired CustomerService customerService;
	@Autowired AddressService addressService;
	@Autowired ShippingRateService shipService;
	@Autowired ShoppingCartService cartService;
	@Autowired OrderService orderService;
	@Autowired SettingService settingService;
	
	
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
	
	//method modified with currency code from settings service and PayPal client id
	//handler method that show checkout page
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		
		//method call
		Customer  customer = getAuthenticatedCustomer(request);
		
		//gets the default address of the customer 
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		//commented out because of defaultAddress.toString() and customer.toString() in the modal
		//boolean usePrimaryAddressAsDefault = false;
		
		//checks if defaultAddress is not null that is if address in the customer
		//address book is used as a shipping address
		if(defaultAddress != null) {
			//address in the address book will be displayed in the checkout page
			model.addAttribute("shippingAddress", defaultAddress.toString());
			
			//gets the shipping rate of the default address
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		}else {
			
			//customer primary address is the address to be displayed in the checkout page
			model.addAttribute("shippingAddress", customer.toString());
			//use primary address as default in case default address is null
			//commented out because of defaultAddress.toString() and customer.toString() in the modal
			//usePrimaryAddressAsDefault = true;
			
			//uses customer address as default address if none of the address in the 
			//address book is used as shipping address
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		//redirect the customer to shopping cart if shipping rate is null
		if(shippingRate == null) {
			return "redirect:/cart";
		}
		

		//list cart item based on a customer
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		//prepare checkout info from checkout service
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		//get currency code. this is related to payment(paypal)
		String currencyCode = settingService.getCurrencyCode();
		
		//get payment settings
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		
		//get PayPal client id
		String paypalClientId = paymentSettings.getClientID();
		
		model.addAttribute("paypalClientId", paypalClientId);
		model.addAttribute("currencyCode", currencyCode);
		model.addAttribute("customer", customer);
		
		//add checkout info for display
		model.addAttribute("checkoutInfo", checkoutInfo);
		
		//add cart items products for display
		model.addAttribute("cartItems", cartItems);
		
 		

		return "checkout/checkout";
	}

	// method that return customer object of the authenticated customer
	private Customer getAuthenticatedCustomer(HttpServletRequest request){
		//get email of the authenticated customer
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		//NB no exception is thrown here because customer must login before s/he can view cart items
		
		return customerService.getCustomerByEmail(email);
	}

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
	
	//method updated with send order confirmation email
	//handler method for placing an order with COD
		@PostMapping("/place_order")
		public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
			
			//get the payment type. NB paymentMethod is the name attribute value "paymentMethod" 
			//of the radio button cash on delivery in checkout page
			String paymentType = request.getParameter("paymentMethod");
			
			//create new payment method
			PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
			
			//method call
			Customer  customer = getAuthenticatedCustomer(request);
			
			//gets the default address of the customer 
			Address defaultAddress = addressService.getDefaultAddress(customer);
			ShippingRate shippingRate = null;
			
			//commented out because of defaultAddress.toString() and customer.toString() in the modal
			//boolean usePrimaryAddressAsDefault = false;
			
			//checks if defaultAddress is not null that is if address in the customer
			//address book is used as a shipping address
			if(defaultAddress != null) {
				//gets the shipping rate of the default address
				shippingRate = shipService.getShippingRateForAddress(defaultAddress);
			}else {	
				//uses customer address as default address if none of the address in the 
				//address book is used as shipping address
				shippingRate = shipService.getShippingRateForCustomer(customer);
			}
			
			
			//list cart item based on a customer
			List<CartItem> cartItems = cartService.listCartItems(customer);
			
			//prepare checkout info from checkout service
			CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
			
			//creates order
			Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
			
			//empty cart after order has been completed
			cartService.deleteByCustomer(customer);
			
			//send order confirmation email
			sendOrderConfirmationEmail(request, createdOrder);
			
			return "/checkout/order_completed";
		}
 
		private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws UnsupportedEncodingException, MessagingException {
			//get email settings from the settings service
			EmailSettingBag emailSettings = settingService.getEmailSettings();
			
			//prepares the email 
			JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
			
			//set default encoding for email so that it can display currency symbols correctly
			mailSender.setDefaultEncoding("utf-8");
			
			//get customer email to send the email to
			String toAddress = order.getCustomer().getEmail();
			
			//get email subject and content from the email settings object
			String subject = emailSettings.getOrderConfirmationSubject();
			String content = emailSettings.getOrderConfirmationContent();
			
			//replace [[orderId]] with the actual order id. [[orderId]] can be found in the 
			//mail template of the order confirmation
			subject = subject.replace("[[orderOd]]", String.valueOf(order.getId()));
			
			//create Mime message object from mail sender
			MimeMessage message = mailSender.createMimeMessage();
			
			//create new Mime message helper
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			//set from address and name of the person sending the email
			helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
			
			helper.setTo(toAddress);
			helper.setSubject(subject);
			
			//replace the place holders in the email content with the actual values
			//in include name, order times ...
			//create new date format object
			DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, MMM yyyy");
			
			//format the order time
			String orderTime = dateFormatter.format(order.getOrderTime());
			
			//get currency settings
			CurrencySettingBag currencySettings = settingService.getCurrencySettings();
			
			//format total amount of the order
			String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);
			
			//update email content by replacing the place holders in the email content with the actual values
			content = content.replace("[[name]]", order.getCustomer().getFullName());	
			content = content.replace("[[orderId]]", String.valueOf(order.getId()));	
			content = content.replace("[[orderTime]]", orderTime);	
			content = content.replace("[[shippingAddress]]", order.getShippingAddress());	
			content = content.replace("[[total]]", totalAmount);	
			content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());	
			
			//set email content. the second parameter (true) indicate the content is html content
			helper.setText(content, true);
			
			//call mail sender to send the message
			mailSender.send(message);
		}
	
}
