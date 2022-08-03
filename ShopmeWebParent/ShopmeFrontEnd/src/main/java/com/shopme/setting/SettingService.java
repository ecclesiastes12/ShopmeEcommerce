package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository settingRepo;
	
	@Autowired CurrencyRepository currencyRepo;

	

	// method that returns general setting object
	public List<Setting> getGeneralSettings() {

		return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	// method that returns mail server and mail template setting object
	//this settings object are set by EmailSettingBag
	public EmailSettingBag getEmailSettings() {
		//return a list of mail server settings from the database
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);	
		
		//appends mail template settings to mail server settings
		settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));
		
		return new EmailSettingBag(settings);//return new EmailSettingBag object
	}
	
	//method that return currency settings
	public CurrencySettingBag getCurrencySettings() {
		
		//grab list of currency settings
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.CURRENCY);
		
		//return new currency settings bag object
		return new CurrencySettingBag(settings);
	}
	
	//method that return payment settings
	public PaymentSettingBag getPaymentSettings() {
		
		//grab list of payment settings
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.PAYMENT);
		
		//return new payment setting bag object
		return new PaymentSettingBag(settings);
	}
	
	//method that return currency code in use. this is related to payment
	public String  getCurrencyCode() {
		//grab currency id from the database
		Setting setting = settingRepo.findByKey("CURRENCY_ID");
		
		//convert the value currency id to integer. this is because in the db the value of CURRENCY_ID is a String
		Integer currencyId = Integer.parseInt(setting.getValue());
		
		//find curreny id from the settings object
		Currency currency = currencyRepo.findById(currencyId).get();
		
		return currency.getCode();
	}
}
