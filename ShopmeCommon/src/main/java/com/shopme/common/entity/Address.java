package com.shopme.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
//public class Address extends IdBasedEntity{
//public class Address extends AbstractAddress{
public class Address extends AbstractAddressWithCountry{
	//IdBasedEntity abstract class change to AbstractAddress because AbstractAddress now extends IdBasedEntity
	//Address class now extends AbstractAddressWithCountry which also extends AbstractAddress 
	
	
	//moved to IdBaseEntity.java
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
	 */
	
	//moved to AbstractAddress.java
	/*
	 * @Column(length = 45, nullable = false, name = "first_name") String firstName;
	 * 
	 * @Column(length = 45, nullable = false, name = "last_name") String lastName;
	 * 
	 * @Column(length = 15, nullable = false, name = "phone_number") String
	 * phoneNumber;
	 * 
	 * @Column(length = 64, nullable = false, name = "address_line1") String
	 * addressLine1;
	 * 
	 * @Column(length = 64, name = "address_line2") String addressLine2;
	 * 
	 * @Column(length = 45, nullable = false) String city;
	 * 
	 * @Column(length = 45, nullable = false) String state;
	 * 
	 * @Column(length = 10, nullable = false) String postalCode;
	 */
	
	//moved to AbstractAddressWithCountry
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "country_id") Country country;
	 */
	@ManyToOne
	@JoinColumn(name = "customer_id")
	Customer customer;
	
	@Column(name = "default_address")
	boolean defaultForShipping;

	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */

	
	//moved to AbstractAddress.java
	/*
	 * public String getFirstName() { return firstName; }
	 * 
	 * public void setFirstName(String firstName) { this.firstName = firstName; }
	 * 
	 * public String getLastName() { return lastName; }
	 * 
	 * public void setLastName(String lastName) { this.lastName = lastName; }
	 * 
	 * public String getPhoneNumber() { return phoneNumber; }
	 * 
	 * public void setPhoneNumber(String phoneNumber) { this.phoneNumber =
	 * phoneNumber; }
	 * 
	 * public String getAddressLine1() { return addressLine1; }
	 * 
	 * public void setAddressLine1(String addressLine1) { this.addressLine1 =
	 * addressLine1; }
	 * 
	 * public String getAddressLine2() { return addressLine2; }
	 * 
	 * public void setAddressLine2(String addressLine2) { this.addressLine2 =
	 * addressLine2; }
	 * 
	 * public String getCity() { return city; }
	 * 
	 * public void setCity(String city) { this.city = city; }
	 * 
	 * public String getState() { return state; }
	 * 
	 * public void setState(String state) { this.state = state; }
	 * 
	 * public String getPostalCode() { return postalCode; }
	 * 
	 * public void setPostalCode(String postalCode) { this.postalCode = postalCode;
	 * }
	 */
	
	//moved to AbstractAddressWithCountry
	/*
	 * public Country getCountry() { return country; }
	 * 
	 * public void setCountry(Country country) { this.country = country; }
	 */

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isDefaultForShipping() {
		return defaultForShipping;
	}

	public void setDefaultForShipping(boolean defaultForShipping) {
		this.defaultForShipping = defaultForShipping;
	}

//	@Override
//	public String toString() {
//		return "Address [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
//				+ phoneNumber + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city
//				+ ", state=" + state + ", postalCode=" + postalCode + ", country=" + country + ", customer=" + customer
//				+ ", defaultForShipping=" + defaultForShipping + "]";
//	}
	
	
	//moved to AbstractAddressWithCountry
	/*
	 * //to string method modified
	 * 
	 * @Override public String toString() { String address = firstName;
	 * 
	 * //check if last name is not empty and not null. because customer can logged
	 * in via google or facebook //meaning those field can be null or empty
	 * if(lastName != null && !lastName.isEmpty()) address += ", " + lastName;
	 * 
	 * if(!addressLine1.isEmpty()) address += ", " + addressLine1;
	 * 
	 * if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " +
	 * addressLine2;
	 * 
	 * if(!city.isEmpty()) address += ", " + city;
	 * 
	 * if(state != null && !state.isEmpty()) address += ", " + state;
	 * 
	 * address += "," + country.getName();
	 * 
	 * if(!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
	 * if(!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;
	 * 
	 * return address; }
	 */
}
