package com.shopme.common.entity.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.IdBasedEntity;

@Entity
@Table(name = "product_details")
public class ProductDetail extends IdBasedEntity{

	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */
		
	
	String name;
	String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	Product product;
	
	public ProductDetail() {}
	
	public ProductDetail(String name, String value, Product product) {
		this.name = name;
		this.value = value;
		this.product = product;
	}
	
	public ProductDetail(Integer id, String name, String value, Product product) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}

	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; } public void setId(Integer id) { this.id
	 * = id; }
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}