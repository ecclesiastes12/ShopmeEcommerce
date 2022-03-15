package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

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
	
	
	
}
