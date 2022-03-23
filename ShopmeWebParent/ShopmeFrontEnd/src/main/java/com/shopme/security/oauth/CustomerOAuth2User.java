package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	//variable declaration of type oauth2
	OAuth2User oauth2User;
	//variable declaration for clientName
	//NB clientName is the name in oauth2 registration thus google or facebook in application.yml
	//so that application will be able to identify which type of client
	String clientName;
	
	//variable declaration for getting customer full name in case customer logged in
	//via facebook or google and update his or her name
	String fullName;
	
	public CustomerOAuth2User(OAuth2User oauth2User,String clientName) {
		this.oauth2User = oauth2User;
		this.clientName = clientName;
	}
	
	
	@Override
	public Map<String, Object> getAttributes() {	
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauth2User.getAttribute("name");//returns the name of the authenticated user
	}
	
	//returns the email of the authenticated user
	public String getEmail() {
		return oauth2User.getAttribute("email");
	}

//	//gets the customer full name when oauth2 authorization is used
//	public String getFullName() {
//		return oauth2User.getAttribute("name");
//	}

	//gets the customer full name when oauth2 authorization is used
		public String getFullName() {
			//return the value of the attribute name in the oauth2User if only the 
			//the fullName value is not null
			return fullName != null ? fullName : oauth2User.getAttribute("name");
		}

	public String getClientName() {
		return clientName;
	}
	
	//for setting customer's full name in case customer logged in via facebook or google
	//and updates his or her name
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
