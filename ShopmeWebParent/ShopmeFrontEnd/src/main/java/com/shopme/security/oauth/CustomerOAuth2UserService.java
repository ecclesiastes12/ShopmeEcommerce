package com.shopme.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {

//	@Override
//	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		OAuth2User user = super.loadUser(userRequest);
//		
//		//returns a new object of CustomerOAuth2User class
//		return new CustomerOAuth2User(user);
//	}
	
	//modified with clientName
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//get the client name through the oauth2 userRequest object thus type of client registration
		String clientName = userRequest.getClientRegistration().getClientName();
		
		OAuth2User user = super.loadUser(userRequest);
		
		//returns a new object of CustomerOAuth2User class
		return new CustomerOAuth2User(user, clientName);
	}

	
}
