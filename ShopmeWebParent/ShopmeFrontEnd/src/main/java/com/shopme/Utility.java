package com.shopme;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.hql.internal.ast.tree.UpdateStatement;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

/*
 * NB don't forget to add java mail dependency(spring-boot-starter-mail) in the pom.xml file
 * The main purpose of the class is to get the site url and 
 * to prepare the mail sender for sending email
 */

public class Utility {

	//static method that get the site url thus the url of the application
	//this url will be used to verify http link in the verification email content
	public static String getSiteURL(HttpServletRequest request) {
		
		//grab the site url. this will return full url of the current request
		//and for security purpose we must delete the actual site url and
		//keep only the site url of the context-path of the application
		String siteURL = request.getRequestURL().toString();
				
		//return only the servlet path of the site url
		return siteURL.replace(request.getServletPath(), "");
	}
	
	//method that configures an instance of java mail sender implementation class
	//with email settings and mail server settings read from the database
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		//create instance of java mail sender implementation class
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		//set settings for mail server
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		//set mail properties smtp authentication and smtp secured connection
		Properties mailProperties = new Properties(); //create properties instance
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		
		//set properties for the mail sender
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;//return mailSender object		
	}
	
	//method that get the email of authenticated customer
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		//get the user principal object that represent authenticated user object
		Object principal = request.getUserPrincipal();
		
		//return null if principal is null. that is when customer has not logged in into the application
		if(principal == null) return null;
		
		String customerEmail = null;
		
		//get instance of customer email if customer logged in with email and password or remember-me
		if(principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
			//get instance of customer name if customer logged in with google or facebook
		}else if(principal instanceof OAuth2AuthenticationToken){
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();//get customer email
		}
		
		return customerEmail;	
	}
		
	// Static method for formatting total amount in the order confirmation email based on currency settings
	
	public static String formatCurrency(float amount, CurrencySettingBag settings) {
		//get currency symbols from the settings object which is in the database
		String symbol = settings.getSymbol();
		
		//get symbol position thus before and after the number
		String symbolPosition = settings.getSymbolPostion();
		
		//get decimal point type
		String decimalPointType = settings.getDecimalPointType();
		
		//get thousands point type
		String thousandsPointType = settings.getThousandsPointType();
		
		//gets the decimal digits
		int decimalDigits = settings.getDecimalDigits();
		
		//contract pattern based on currency settings
		//get the position of the currency symbol before the price
		String pattern = symbolPosition.equals("Before price") ? symbol : "";
		//contract pattern
		pattern += "###,###";
		
		//check the number of digits after decimal point
		if(decimalDigits > 0) {
			//updates the pattern with a point type decimal separator if the decimal digits is geater than 0
			pattern += "."; 		
		//iterate through the number of decimal digits and append it to the pattern
			for(int count = 1; count <= decimalDigits; count++) pattern += "#";
	
		}
		
		//contract pattern if currency symbol is after the price
		pattern += symbolPosition.equals("After price") ? symbol : "";
		
		//update decimal format symbols based on decimalPointType and thousandPointType
		char thousandSeparator = thousandsPointType.equals("POINT") ? '.' : ',';
		char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';
		/*
		 * , represent group or thousand separator
		 * . represent decimal separator
		 *  the last ## represent the figures after the decimal separator
		 */
		//currency pattern 
		//String pattern = "###,###.##";
		
		//get instance of decimal format symbol
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		
		//set decimal and thousand separator for symbol
		//decimalFormatSymbols.setDecimalSeparator(',');
		//thousands separator
		//decimalFormatSymbols.setGroupingSeparator('.'); 
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormatSymbols.setGroupingSeparator(thousandSeparator); 
		
		//creates new decimal format object
		DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
		
		
		return formatter.format(amount);
	}
	
	
}
