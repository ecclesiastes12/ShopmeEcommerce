package com.shopme.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * NB this entity is updated with another variable of type authentication.
 * this is purposely for other types of authentication
 */
@Entity
@Table(name = "customers")
//public class Customer extends AbstractAddress{
public class Customer extends AbstractAddressWithCountry{

	//Customer class now extends AbstractAddressWithCountry which also extends AbstractAddress 
	
	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */
	
	
	@Column(length = 45, nullable = false, unique = true)
	String email;
	
	@Column(length = 64, nullable = false)
	String password;
	
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
	@Column(nullable = false, name = "created_time")
	Date createdTime;
	
	boolean enabled;
	
	@Column(name = "verification_code", length = 64)
	String verificationCode;
	
	//moved to AbstractAddressWithCountry
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "country_id") Country country;
	 */
	
	@Enumerated(EnumType.STRING)//meaning the enumerated variable is of type string
	@Column(name = "authentication_type", length =10)
	AuthenticationType authenticationType;
	
	@Column(name = "reset_password_token", length = 30)
	String resetPasswordToken;

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public Customer() {
	
	}
	
	public Customer(Integer id) {
		this.id = id;
	}

	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	//moved to AbstractAddressWithCountry
	/*
	 * public Country getCountry() { return country; }
	 * 
	 * public void setCountry(Country country) { this.country = country; }
	 */

	
	//commented out because it will now inherit it from AbstractAddressWithCountry
	/*
	 * @Override public String toString() { return "Customer [id=" + id + ", email="
	 * + email + ", password=" + password + ", firstName=" + firstName +
	 * ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", addressLine1="
	 * + addressLine1 + ", addressLine2=" + addressLine2 + ", city=" + city +
	 * ", state=" + state + ", postalCode=" + postalCode + ", createdTime=" +
	 * createdTime + ", enabled=" + enabled + ", verificationCode=" +
	 * verificationCode + ", country=" + country + "]"; }
	 */
	
	
	//method that return full name of the customer
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
//	//method that return full name of the customer
//		public String getFullName() {
//			return getFirstName() + " " + getLastName();
//		}

		public AuthenticationType getAuthenticationType() {
			return authenticationType;
		}

		public void setAuthenticationType(AuthenticationType authenticationType) {
			this.authenticationType = authenticationType;
		}

	//moved to AbstractAddressWithCountry
	//for customer address book
	/*
	 * @Transient public String getAddress() { String address = firstName;
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
