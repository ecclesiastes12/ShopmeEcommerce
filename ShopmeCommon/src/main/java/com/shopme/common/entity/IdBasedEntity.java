package com.shopme.common.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/*
 * NB Once a class is of type abstract, an object of that class
 * can not be created. Abstract class are super class and they 
 * contain variable and method that is inherited by other classes
 * 
 * @MappedSuperclass indicate this class is a super class
 */

@MappedSuperclass
public abstract class IdBasedEntity {
	//NB for inheritance purpose the access type of the variable should be change to protected
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	

}
