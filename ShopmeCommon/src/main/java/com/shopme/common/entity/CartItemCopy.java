//package com.shopme.common.entity;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import com.shopme.common.entity.product.Product;
//
//@Entity
//@Table(name = "cart_items")
//public class CartItemCopy extends IdBasedEntity{
//
//	//moved to IdBaseEntity.java
//	/*
//	 * @Id
//	 * 
//	 * @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
//	 */
//	
//	@ManyToOne
//	@JoinColumn(name = "customer_id") //NB customer_id is a primary key in customer table
//	Customer customer;
//	
//	@ManyToOne
//	@JoinColumn(name = "product_id") //NB product_id is a primary key in customer table
//	Product product;
//	
//	int quantity;
//		
//	public CartItemCopy() {
//
//	}
//
//	//moved to IdBaseEntity.java
//	/*
//	 * public Integer getId() { return id; } public void setId(Integer id) { this.id
//	 * = id; }
//	 */
//	public Customer getCustomer() {
//		return customer;
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}
//	public Product getProduct() {
//		return product;
//	}
//	public void setProduct(Product product) {
//		this.product = product;
//	}
//	public int getQuantity() {
//		return quantity;
//	}
//	public void setQuantity(int quantity) {
//		this.quantity = quantity;
//	}
//	
//	
////	@Override
////	public String toString() {
////		return "CartItem [id=" + id + ", customer=" + customer + ", product=" + product + ", quantity=" + quantity
////				+ "]";
////	}
//	
//	//method modified to print customer full name and product short name
//	@Override
//	public String toString() {
//		return "CartItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() + ", quantity=" + quantity
//				+ "]";
//	}
//	
//	//method that return subtotal of cart items
//	//for cart item
//	@Transient
//	public float getSubtotal() {
//		return product.getDiscountPrice() * quantity;
//	}
//	
//	
//}
