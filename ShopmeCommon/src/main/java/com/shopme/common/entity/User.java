package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="users")
public class User extends IdBasedEntity{

	//moved to IdBaseEntity.java
	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */
	
	@Column(length = 128, nullable=false, unique=true)
	String email;
	
	@Column(length = 64, nullable=false)
	String password;
	
	@Column(name="first_name", length= 45, nullable=false)
	String firstName;
	
	@Column(name="last_name", length= 45, nullable=false)
	String lastName;
	
	@Column(length= 64)
	String photos;
	
	boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER )
	@JoinTable(
			name="users_roles",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	Set<Role> roles = new HashSet();

	public User() {}
	
	public User(String email, String password, String firstName, String lastName) {
		
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	//method to add role to user
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roles=" + roles + "]";
	}
	
	//@Transient indicates that this method is not to any field in the database
	@Transient
	public String getPhotosImagePath() {
		//return default photo image if id or photo is null
		if(id == null || photos == null) return "/images/default-user.png";
		
		return "/user-photos/" + this.id + "/" + this.photos;
	}
	
	//toString method to display user full name
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	//method to check the role name of a user
	public boolean hasRole(String roleName) {
		//gets all the role objects of the set roles by Iteration
		Iterator<Role> iterator = roles.iterator();
		
		//iterate each of the role object in the iterator
		while(iterator.hasNext()) {
			//gets the role object of the next element in the iterator
			Role role = iterator.next();
			
			//check if the role name is equal to the paramter roleName
			if(role.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
}
