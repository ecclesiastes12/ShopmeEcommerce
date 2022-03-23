package com.shopme.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

@Component
public class OAuth2loginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired CustomerService customerService;
	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws ServletException, IOException {
//		//get the principal through the authentication object.
//		//authentication.getPrincipal() is cast to CustomerOAuth2User because CustomerOAuth2UserService class
//		//returns new instants of CustomerOAuth2User
//		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
//		
//		//prints name and email of authenticated oauth2User object for testing purpose
//		String name = oauth2User.getName();
//		String email = oauth2User.getEmail();
//		
//		//this will return code of the country of the current locale based on the locale in the customers browser
//		String countryCode = request.getLocale().getCountry();
//		
//		System.out.println("OAuth2loginSuccessHandler: " + name + " | " + email);
//
//		
//		//get customer by email
//		Customer customer = customerService.getCustomerByEmail(email);
//		
//		//check if customer is null
//		if(customer == null) {
//			//adds customer name and email to the db if the customer doesn't exist in the db
//			//thus if login is through OAuth2 eg. google
//			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);
//		}else {
//			//updates the authentication type in the db if customer logged in via google
//			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
//		}
//		
//		super.onAuthenticationSuccess(request, response, authentication);
//	}
	
	//method modified with authentication type based on client name
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		//get the principal through the authentication object.
		//authentication.getPrincipal() is cast to CustomerOAuth2User because CustomerOAuth2UserService class
		//returns new instants of CustomerOAuth2User
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
		
		//prints name and email of authenticated oauth2User object for testing purpose
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		
		//this will return code of the country of the current locale based on the locale in the customers browser
		String countryCode = request.getLocale().getCountry();
		
		String clientName = oauth2User.getClientName();
		
		//System.out.println("OAuth2loginSuccessHandler: " + name + " | " + email);
		//System.out.println("Client name: " + clientName );

		
		//get customer by email
		Customer customer = customerService.getCustomerByEmail(email);
		
		//get the authentication type based on client name
		AuthenticationType authenticationType = getAuthenticationType(clientName); //method call
		
		//check if customer is null
		if(customer == null) {
			//adds customer name and email to the db if the customer doesn't exist in the db
			//thus if login is through OAuth2 eg. google
			//customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authenticationType);
		}else {
			//update the customer name in the CustomerOAuth2User to the name of the customer in the db
			//instead of the customer name used in google or facebook registration. Without this the 
			//customer name will always be the name used in google or facebook registration if the 
			//authentication type is facebook or google
			oauth2User.setFullName(customer.getFullName());
			
			//updates the authentication type in the db if customer logged in via google
			//customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
			customerService.updateAuthenticationType(customer, authenticationType);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	//method to get authentication type based on client name
	private AuthenticationType getAuthenticationType(String clientName) {
		//checks client name to return the authentication type
		if(clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		}else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		}else {
			return AuthenticationType.DATABASE;
		}
	}
}
