package com.shopme.security;

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
import com.shopme.security.oauth.CustomerOAuth2User;

/*
 * NB this class is for login success if the customer logged in with the credentials in the database
 */

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		//get the principal through the authentication object.
		//authentication.getPrincipal() is cast to CustomerUserDetails because CustomerUserDetails class
		//wraps an instants of Customer in CustomerUserDetails constructor
		CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
		
		//get the customer
		Customer customer = userDetails.getCustomer();
		
		//updates the authentication type in the db if customer logged in via customer credentials in the db
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	
	
	
}
