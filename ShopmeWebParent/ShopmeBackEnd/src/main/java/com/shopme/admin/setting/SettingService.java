package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository repo;

	public List<Setting> listAllSettings() {
		return (List<Setting>) repo.findAll();
	}

	// method that returns general setting bag object
	public GeneralSettingBag getGeneralSettings() {

		//get list collection of settings in an array list format
		List<Setting> settings = new ArrayList<>();
		
		//find list of setting by category from the db. thus GENERAL 
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		
		//find list of setting by category from the db. thus CURRENCY
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);
		
		//add the list of generalSettings to the  settings
		settings.addAll(generalSettings);
		
		////add the list of currencySettings to the  settings
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);//method call
	}

	//method that saves list of settings object
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}
	
	//method that returns a list of mail server settings
	public List<Setting> getMailServerSettings(){
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}

	//method that returns a list of mail templates settings
	public List<Setting> getMailTemplateSettings() {
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
	
	//method that load currency settings
	public List<Setting> getCurrencySettings(){
		return repo.findByCategory(SettingCategory.CURRENCY);
	}
	
	//Business method that get payment settings
	public List<Setting> getPaymentSettings(){	
		return repo.findByCategory(SettingCategory.PAYMENT);
	}
}
