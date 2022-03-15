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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
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
		
		//method call
		sendVerificationEmail(request, customer);
		
		customerService.registerCustomer(customer);//register the customer
		
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
}
