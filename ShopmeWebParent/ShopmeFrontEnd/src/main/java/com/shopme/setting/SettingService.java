package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository repo;

	

	// method that returns general setting object
	public List<Setting> getGeneralSettings() {

		return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	// method that returns mail server and mail template setting object
	//this settings object are set by EmailSettingBag
	public EmailSettingBag getEmailSettings() {
		//return a list of mail server settings from the database
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);	
		
		//appends mail template settings to mail server settings
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));
		
		return new EmailSettingBag(settings);//return new EmailSettingBag object
	}
	
}
