package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	CustomerRepository customerRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//method call from customer repository
		Customer customer = customerRepo.findCustomerByEmail(email);
		
//		//checks if user is not null
//		if(customer != null) {
//			return new CustomerUserDetails(customer);
//		}
//		
//		//throws error message if user email is not found
//		throw new UsernameNotFoundException("No customer found with the email: " + email);
		
		if (customer == null) 
			throw new UsernameNotFoundException("No customer found with the email " + email);
		
		return new CustomerUserDetails(customer);
	}

	
}
