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
		
		//this will return code of the country of the current locale in the customers browser
		String countryCode = request.getLocale().getCountry();
		
		System.out.println("OAuth2loginSuccessHandler: " + name + " | " + email);
		
		//get customer by email
		Customer customer = customerService.getCustomerByEmail(email);
		
		//check if customer is null
		if(customer == null) {
			//adds customer name and email to the db if the customer doesn't exist in the db
			//thus if login is through OAuth2 eg. google
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode);
		}else {
			//updates the authentication type in the db if customer logged in via google
			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
