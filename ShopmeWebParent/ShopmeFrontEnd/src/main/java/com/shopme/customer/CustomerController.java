package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;


@Controller
public class CustomerController {

	@Autowired CustomerService customerService;
	@Autowired SettingService settingService;
	
	//handler method to show customer registration form
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		
		//list all countries
		List<Country> listCountries = customerService.listAllCountries();
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());
		
		
		return "register/register_form";
	}


	//handler method to register customers
	@PostMapping("/create_customer")
	public String registerCustomer(Customer customer, Model model, HttpServletRequest request) 
			throws UnsupportedEncodingException, MessagingException {
		
		model.addAttribute("pageTitle", "Registration succeeded!");
		
		customerService.registerCustomer(customer);//register the customer
		
		//method call
		sendVerificationEmail(request, customer);
		
		return "register/register_success";
	}


	//method that send registration verification email to the customer email
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) 
			throws UnsupportedEncodingException, MessagingException {
		//grab email settings from EmailSettingBag object
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		//grab mail sender settings for sending email from Utility class
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		//get the customer email address to send the verification email
		String toAddress = customer.getEmail();
		
		//get email subject for sending the verification mail from email settings
		String subject = emailSettings.getCustomerVerifySubject();
		
		//get email content for sending the verification mail from email settings
		String content = emailSettings.getCustomerVerifyContent();
		
		//create mime message object for sending html email
		MimeMessage message = mailSender.createMimeMessage();
		
		//create spring boot mail with MimeMessageHelper
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		//set senders email address and name to send the message
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		
		//set recipient address(email)
		helper.setTo(toAddress);
		
		//set subject of the email
		helper.setSubject(subject);
		
		//update the email content by replacing the name placeholder with the name of the customer
		content = content.replace("[[name]]", customer.getFullName());		
		
		//replace the second placeholder of the url verify link
		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("verifyURL: " + verifyURL);
		
		
	}

	//method  to verify customer account
	//@Param("code") binds the value of the verification code in the parameter url
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model model) {
		//verifies the customer
		boolean verified = customerService.verify(code);
		
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
	
//	//handler method for customer details
//	@GetMapping("/account_details")
//	public String viewAccountDetails(Model model, HttpServletRequest request) {
////		//get the user principal object that represent authenticated user object
////		Object principal = request.getUserPrincipal();
////		//get the principal name
////		String principalType = principal.getClass().getName();
////		
////		System.out.println("Prinicipal name: " + request.getUserPrincipal().getName());
////		System.out.println(principalType);
////		//NB if customer logged in with email and password the Principal name is the customer email and princiapalType is UsernamePasswordAuthenticationToken
////		//NB if customer logged in with google or facebook the Principal name is the customer name and princiapalType is OAuth2AuthenticationToken
////		//NB if customer logged in with remember-me the Principal name is the customer email and princiapalType is RememberMeAuthenticationToken
//	
//		return "customer/account_form";
//	}
	
	//handler method for displaying customer details form
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		//get email of authenticated customer. method call
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		//get customer object from the customerService class
		Customer customer = customerService.getCustomerByEmail(email);
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);
		
		
		return "customer/account_form";
	}
	
	
	//method moved to Utility.java and modified
//	//method that get the email of authenticated customer
//	private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
//		//get the user principal object that represent authenticated user object
//		Object principal = request.getUserPrincipal();
//			
//		String customerEmail = null;
//		
//		//get instance of customer email if customer logged in with email and password or remember-me
//		if(principal instanceof UsernamePasswordAuthenticationToken
//				|| principal instanceof RememberMeAuthenticationToken) {
//			customerEmail = request.getUserPrincipal().getName();
//			//get instance of customer name if customer logged in with google or facebook
//		}else if(principal instanceof OAuth2AuthenticationToken){
//			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
//			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
//			customerEmail = oauth2User.getEmail();//get customer email
//		}
//		
//		return customerEmail;	
//	}
//	
	
	
	//handler method for updating customer details
	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {
		customerService.update(customer);
		ra.addFlashAttribute("message", "Your account details have been updated!");
		
		//method call
		updateNameForAuthenticatedCustomer(customer, request);
		
		//creates parameter request for redirecting the page
		String redirectOption = request.getParameter("redirect");
		
		//redirect to account details page if customer update the account from the accounts page
		String redirectURL = "redirect:/account_details";
		
		//checks if customer is updating the account details from the address book
		if("address_book".equals(redirectOption)) {
			
			//redirect the page back to address book page if the update of account details is done from the address book page
			redirectURL = "redirect:/address_book";
			
			//checks if customer is updating the account details from the cart page
		}else if("cart".equals(redirectOption)) {
			
			//redirect the page back to cart page if the update of account details is done from the cart page
			redirectURL = "redirect:/cart";
		}else if("checkout".equals(redirectOption)) {
			
			//redirect the page back to checkout page if the update of account details is done from the checkout page
			redirectURL = "redirect:/address_book?redirect=checkout";
		}
		
		return redirectURL;		
	}


	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		//get the user principal object that represent authenticated user object
		Object principal = request.getUserPrincipal();
		
		
		//get instance of customer email if customer logged in with email and password or remember-me
		if(principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			
			//get the customer object in the CustomerUserDetails class
			Customer authenticatedCustomer = userDetails.getCustomer();
			
			//set customer first and last name
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
			
			//get instance of customer name if customer logged in with google or facebook
		}else if(principal instanceof OAuth2AuthenticationToken){
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			
			//get customer full name
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			
			oauth2User.setFullName(fullName);//set customer full name
		}
	}
	
	//method to get CustomerUserDetails object
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		//get the CustomerUserDetails object via the principal
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			//assign the principal to the UsernamePasswordAuthenticationToken variable
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			
			//get customer user details
			userDetails = (CustomerUserDetails) token.getPrincipal();
			
			//check if the principal object is an instance of RememberMeAuthenticationToken
		}else if (principal instanceof RememberMeAuthenticationToken) {
			//get the CustomerUserDetails object via the principal
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			
			//get customer user details
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
	
		
		return userDetails;
	}
		
	
	
	
	
	
	
	
}
