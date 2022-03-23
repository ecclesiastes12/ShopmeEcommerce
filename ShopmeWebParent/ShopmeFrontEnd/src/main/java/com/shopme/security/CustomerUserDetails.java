package com.shopme.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Role;


public class CustomerUserDetails implements UserDetails{
	Customer customer;
	
	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}
	
	//NB since customers doesn't have any specific role getAuthorities should return null
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		return null;
	}

	@Override
	public String getPassword() {
		
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled(); 
	}

	//for customer full name
	public String getFullName() {
		return customer.getFirstName() + " " + customer.getLastName();
	}
	
	//returns customer object. this is for DatabaseLoginSuccessHander class
	public Customer getCustomer() {
		return this.customer;
	}
//	
//	//we need to set first name and last name
//	//in order to be able to update customer first and 
//	//last name in the menu bar. when we use
//	//@AuthenticationPrincipal annotation
//	
//	public void setFirstName(String firstName) {
//		 this.customer.setFirstName(firstName);
//	}
//	
//	public void setLastName(String lastName) {
//		this.customer.setLastName(lastName);
//		
//	}


}
