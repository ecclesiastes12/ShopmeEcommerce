package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties.Settings;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

@Controller
public class ForgotPasswordController {

	@Autowired CustomerService customerService;
	@Autowired SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	//handler method to process the request form
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		//get the email parameter through HttpServletRequest
		String email = request.getParameter("email");
		
		try {
			String token = customerService.updatePasswordResetToken(email);
			
			//password reset link
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			
//			System.out.println("email: " + email);
//			System.out.println("token: " + token);
			
			//send the email
			sendEmail(link, email);
			model.addAttribute("message", "We have send a reset password to your email. "
					+ " Please check");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException  | MessagingException e) {
			model.addAttribute("error", "Could not send email");
		}
		
		return "customer/forgot_password_form";
	}
	
	//method that send password reset email
	private void sendEmail(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		//grab email settings from EmailSettingBag object
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		//grab mail sender settings for sending email from Utility class
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		//get the customer email address to send the verification email
		String toAddress = email;
		
		//get email subject for sending the verification mail from email settings
		//String subject = emailSettings.getCustomerVerifySubject();
		String subject = "Here is the link to reset your password";
		
		//get email content for sending the verification mail from email settings
		//String content = emailSettings.getCustomerVerifyContent();
		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password</p>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p>Ignore this email if you do remember you password, "
				+ "or you have not made the request</p>"; 
		
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
		
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	//handler method to display password reset form
	@GetMapping("/reset_password")
	//public String showResetForm(@Param("token") String token, Model model) {
	public String showResetForm(String token, Model model) {
		//grab the password reset token
		Customer customer = customerService.getByResetPasswordToken(token);

		//check if customer with the given token exist
		if(customer != null) {
			model.addAttribute("token", token);
		}else {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		return "customer/reset_password_form";
	}
	
	//method handler for processing password reset
	@PostMapping("/reset_password")
	public String processRestForm(HttpServletRequest request, Model model) {
		//grab the token and new password from the reset_password_form
		//NB "token" references name="token" in reset_password_form and "password" references name="password" in reset_password_form
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle", "Reset Your Password.");
			model.addAttribute("title", "Reset Your Password.");
			model.addAttribute("message", "You have successfully change your password.");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Invalid Token");
			model.addAttribute("message", e.getMessage());
			return "message";
		}
		
	}
}
