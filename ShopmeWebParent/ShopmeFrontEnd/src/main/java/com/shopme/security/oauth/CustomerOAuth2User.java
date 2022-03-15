package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	//variable declaration of type oauth2
	OAuth2User oauth2User;
	
	public CustomerOAuth2User(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
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

	//gets the customer full name when oauth2 authorization is used
	public String getFullName() {
		return oauth2User.getAttribute("name");
	}
}
