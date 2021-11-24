package com.shopme.common.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Setting {

	@Id
	@Column(name ="`key`", nullable = false, length = 128) //"`key`"  is used here because key is a reserved word in mysql db
	String key;
	
	@Column(nullable = false, length = 1024)
	String value;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	SettingCategory category;
	

	public Setting() {
		
	}

	
	public Setting(String key) {
		this.key = key;
	}


	public Setting(String key, String value, SettingCategory category) {
		this.key = key;
		this.value = value;
		this.category = category;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SettingCategory getCategory() {
		return category;
	}

	public void setCategory(SettingCategory category) {
		this.category = category;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}


	//toString method of to display the actual values for key and value
	@Override
	public String toString() {
		return "Setting [key=" + key + ", value=" + value + "]";
	}


	
	
}
