package com.shopme.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer>{

	//NB No custom query for findByCountryAndState because ShippingRate.java has fields
	// or declared variables country and state. By so doing spring will understand the method findByCountryAndState
	
	//method that find shipping rate by country and state
	public ShippingRate findByCountryAndState(Country country, String state);
}
