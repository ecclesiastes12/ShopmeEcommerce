package com.shopme.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;

@Service
public class ShippingRateService {

	@Autowired
	ShippingRateRepository repo;
	
	//method that get the shipping rate for a customer
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		
		//grabs customer state
		String state = customer.getState();
		
		//checks if state is empty or null
		if(state == null || state.isEmpty()) {
			//get city if state is empty or null
			state = customer.getCity();	
		}
		
		return repo.findByCountryAndState(customer.getCountry(), state);
	}
	
	
	//method that get the shipping rate for address
	public ShippingRate getShippingRateForAddress(Address address) {
		
		//gets customer state from address
		String state = address.getState();
		
		//checks if state from address is empty or null
		if(state == null || state.isEmpty()) {
			//get city from address if state from address is empty or null
			state = address.getCity();	
		}
		
		return repo.findByCountryAndState(address.getCountry(), state);
	}
	
	
	
}
