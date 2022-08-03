package com.shopme.common.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/*
 * NB Once a class is of type abstract, an object of that class
 * can not be created. Abstract class are super class and they 
 * contain variable and method that is inherited by other classes
 * 
 * @MappedSuperclass indicate this class is a super class
 */

@MappedSuperclass
public class AbstractAddressWithCountry extends AbstractAddress{

	//NB for inheritance purpose the access type of the variable should be change to protected
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	protected Country country;
	
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	

	//to string method modified
	@Override
	public String toString() {
		String address = firstName;
		
		//check if last name is not empty and not null. because customer can logged in via google or facebook
		//meaning those field can be null or empty
		if(lastName != null && !lastName.isEmpty()) address += ", " + lastName;
		
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		
		if(!city.isEmpty()) address += ", " + city;
		
		if(state != null && !state.isEmpty()) address += ", "  + state;
		
		address += "," + country.getName();
		
		if(!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;
		
		return address;
	}
}
