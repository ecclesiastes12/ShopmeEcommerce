package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2loginSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired CustomerOAuth2UserService oAuth2UserService;
	@Autowired OAuth2loginSuccessHandler oauth2loginHandler;
	@Autowired DatabaseLoginSuccessHandler databaseLoginHandler;
	
	//creates bean for UserDetailsService
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	
	//creates bean for password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//method that configures the authentication provider
	//DaoAuthenticationProvider tell spring security that
	//authentication will be base on the database
	public DaoAuthenticationProvider authenticationProvider() {
		
		//creates new DaoAuthenticationProvider object
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		//sets userDetailsService for DaoAuthenticationProvider
		authProvider.setUserDetailsService(userDetailsService());
		
		//sets passwordEncoder for DaoAuthenticationProvider
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
//	}


	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/customer").authenticated() 
//				.anyRequest().permitAll() 
//				.and()
//				.formLogin()
//					.loginPage("/login")
//					//spring security by default will use username for that parameter
//					//since we use email for the login parameter we have to change customer
//					//parameter name to email 
//					.usernameParameter("email") 
//					.permitAll()
//			.and().logout().permitAll()
//			//for remember me 
//			//the purpose of using key is make the token survive even if
//			//the application is restarted else the user will have to 
//			//login again 
//			.and()
//				.rememberMe()
//					.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
//					//expiration time for the token thus 1 week
//					.tokenValiditySeconds(14 * 24 * 60 * 60);
//		
//	}
	
//	//configure method updated with auth2login
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/customer").authenticated() 
//				.anyRequest().permitAll() 
//				.and()
//				.formLogin()
//					.loginPage("/login")
//					//spring security by default will use username for that parameter
//					//since we use email for the login parameter we have to change customer
//					//parameter name to email 
//					.usernameParameter("email") 
//					.permitAll()
//				.and()
//				//for oauth2 login
//				.oauth2Login()
//					.loginPage("/login")
//					.userInfoEndpoint()
//					.userService(oAuth2UserService)
//					.and()
//				.and()
//				.logout().permitAll()
//				//for remember me 
//				//the purpose of using key is make the token survive even if
//				//the application is restarted else the user will have to 
//				//login again 
//				.and()
//					.rememberMe()
//						.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
//						//expiration time for the token thus 1 week
//						.tokenValiditySeconds(14 * 24 * 60 * 60);
//			
//	}
	
	
	//configure method updated with oath2LoginSuccessHander and databaseLoginHandler
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/customer").authenticated() 
				.antMatchers("/account_details", "/update_account_details", 
						"/cart", "/address_book/**", "/checkout", "/place_order").authenticated()
				.anyRequest().permitAll() 
				.and()
				.formLogin()
					.loginPage("/login")
					//spring security by default will use username for that parameter
					//since we use email for the login parameter we have to change customer
					//parameter name to email 
					.usernameParameter("email") 
					//configures handler for databaseLoginHandler
					.successHandler(databaseLoginHandler)
					.permitAll()
				.and()
				//for oauth2 login
				.oauth2Login()
					.loginPage("/login")
					.userInfoEndpoint()
					.userService(oAuth2UserService)
					.and()
					//configures handler for oath2LoginSuccessHander
					.successHandler(oauth2loginHandler)
				.and()
				.logout().permitAll()
				//for remember me 
				//the purpose of using key is make the token survive even if
				//the application is restarted else the user will have to 
				//login again 
				.and()
					.rememberMe()
						.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
						//expiration time for the token thus 1 week
						.tokenValiditySeconds(14 * 24 * 60 * 60)
					.and()
						//this will allow spring boot to create session when necessary
						.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
			
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// ignores the authentication of static resources
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

	
	
}

