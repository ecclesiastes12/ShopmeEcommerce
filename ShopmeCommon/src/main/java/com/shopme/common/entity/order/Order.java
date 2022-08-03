package com.shopme.common.entity.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.IdBasedEntity;

@Entity
@Table(name = "orders")
//public class Order extends IdBasedEntity{
public class Order extends AbstractAddress{

	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
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
	
	//NB country here is of type string because we want to record country of the 
	//recipient as the time of purchase. This is mainly because the country of the 
	//recipient can be change over time
	@Column(nullable = false, length = 45)
	String country;
	
	Date orderTime;
	
	float shippingCost;
	float productCost;
	float subtotal;
	float tax;
	float total;
	
	int deliveryDays;
	Date deliveryDate;
	
	@Enumerated(EnumType.STRING)
	PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	OrderStatus status;
	
	//reference to customer who place the order
	//many-to-one because one customer can place one or more orders
	@ManyToOne
	@JoinColumn(name = "customer_id")
	Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	Set<OrderDetail> orderDetails = new HashSet<>();

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
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(int deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	//method that copy customer's address
	public void copyAddressFromCustomer() {
		
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
		setState(customer.getState());
	}

//	@Override
//	public String toString() {
//		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod
//				+ ", status=" + status + ", customer=" + customer + "]";
//	}
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod
				+ ", status=" + status + ", customer=" + customer.getFullName() + "]";
	}
	
	//method that set destination
	@Transient
	public String getDestination() {
//		if(state.isEmpty()) {
//			return country + ", " + city;
//		}else {
//			return country + ", " + state + ", " + city;
//		}
//		
		String destination =  city + ", ";
		if (state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;
		
		return destination;
		
	}
	
	@Transient
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public void copyShippingAddress(Address address) {

		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());
		
	}
	
	@Transient
	public String getShippingAddress() {
		String address = firstName;
		
		//check if last name is not empty and not null. because customer can logged in via google or facebook
		//meaning those field can be null or empty
		if(lastName != null && !lastName.isEmpty()) address += ", " + lastName;
		
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		
		if(!city.isEmpty()) address += ", " + city;
		
		if(state != null && !state.isEmpty()) address += ", "  + state;
		
		address += "," + country;
		
		if(!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;
		
		return address;
	}
}
