package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
public class Currency extends IdBasedEntity{

	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */
	
	@Column(length = 64, nullable = false)
	String name;
	
	@Column(length = 3, nullable = false)
	String symbol;
	
	@Column(length = 4, nullable = false)
	String code;
	
	public Currency() {}

	public Currency(String name, String symbol, String code) {
		
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}

	/*
	 * public int getId() { return id; }
	 * 
	 * public void setId(int id) { this.id = id; }
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		//return "Currency [id=" + id + ", name=" + name + ", symbol=" + symbol + ", code=" + code + "]";
		//toString method update to show name, code and symbol in the settings.html
		return name + " - " + code + " - " + symbol;
	}
	
	
	
}
