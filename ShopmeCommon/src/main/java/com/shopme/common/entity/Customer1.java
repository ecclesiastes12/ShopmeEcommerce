//package com.shopme.common.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//
//@Entity
//@Table(name = "customers")
//public class Customer1 {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	Integer id;
//	
//	@Column(length = 45, nullable = false, unique = true)
//	String email;
//	
//	@Column(length = 64, nullable = false)
//	String password;
//	
//	@Column(length = 45, nullable = false, name = "first_name")
//	String firstName;
//	
//	@Column(length = 45, nullable = false, name = "last_name")
//	String lastName;
//	
//	@Column(length = 15, nullable = false, name = "phone_number")
//	String phoneNumber;
//	
//	@Column(length = 64, nullable = false, name = "address_line1")
//	String addressLine1;
//	
//	@Column(length = 64, name = "address_line2")
//	String addressLine2;
//	
//	@Column(length = 45, nullable = false)
//	String city;
//
//	@Column(length = 45, nullable = false)
//	String state;
//	
//	@Column(length = 10, nullable = false)
//	String postalCode;
//	
//	@Column(nullable = false, name = "created_time")
//	Date createdTime;
//	
//	boolean enabled;
//	
//	@Column(length = 64, name = "verification_code")
//	String verificationCode;
//	
//	@ManyToOne
//	@JoinColumn(name = "country_id")
//	Country country;
//
//	public Customer1() {
//	
//	}
//
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getPhoneNumber() {
//		return phoneNumber;
//	}
//
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//
//	public String getAddressLine1() {
//		return addressLine1;
//	}
//	
//	public void setAddressLine1(String addressLine1) {
//		this.addressLine1 = addressLine1;
//	}
//
//	public String getAddressLine2() {
//		return addressLine2;
//	}
//
//	public void setAddressLine2(String addressLine2) {
//		this.addressLine2 = addressLine2;
//	}
//
//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getPostalCode() {
//		return postalCode;
//	}
//
//	public void setPostalCode(String postalCode) {
//		this.postalCode = postalCode;
//	}
//
//	public Date getCreatedTime() {
//		return createdTime;
//	}
//
//	public void setCreatedTime(Date createdTime) {
//		this.createdTime = createdTime;
//	}
//
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public String getVerificationCode() {
//		return verificationCode;
//	}
//
//	public void setVerificationCode(String verificationCode) {
//		this.verificationCode = verificationCode;
//	}
//
//	public Country getCountry() {
//		return country;
//	}
//
//	public void setCountry(Country country) {
//		this.country = country;
//	}
//
//
//
//	@Override
//	public String toString() {
//		return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
//				+ ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", addressLine1=" + addressLine1
//				+ ", addressLine2=" + addressLine2 + ", city=" + city + ", state=" + state + ", postalCode="
//				+ postalCode + ", createdTime=" + createdTime + ", enabled=" + enabled + ", verificationCode="
//				+ verificationCode + ", country=" + country + "]";
//	}
//	
//	
////	//method that return full name of the customer
////	@Transient
////	public String getFullName() {
////		return firstName + " " + lastName;
////	}
//	
//	//method that return full name of the customer
//		@Transient
//		public String getFullName() {
//			return getFirstName() + " " + getLastName();
//		}
//	
//	
//}
