package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	//method that read settings for sending an email that include MAIL_HOST, PORT NUMBET etc in the database
	//return mail host
	public String getHost() {
		return super.getValue("MAIL_HOST");//MAIL_HOST is a key from the settings table
	}
	
	//return mail port
	public int getPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}
	
	//return mail username
	public String getUsername() {
		return super.getValue("MAIL_USERNAME");//MAIL_USERNAME is a key from the settings table
	}
	
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSenderName() {
		return super.getValue("MAIL_SENDER_NAME");
	}
	
	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}
	
	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}
	
	public String getOrderConfirmationSubject() {
		return super.getValue("ORDER_CONFIRMATION_SUBJECT");
	}
	
	public String getOrderConfirmationContent() {
		return super.getValue("ORDER_CONFIRMATION_CONTENT");
	}

}