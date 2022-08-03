package com.shopme.common.entity.setting;

import java.util.List;

public class SettingBag {

	List<Setting> listSettings;
	
	public SettingBag(List<Setting> listSettings) {
		this.listSettings = listSettings;
	}
	
	//method that will return setting object by key
	public Setting get(String key) {
		int index = listSettings.indexOf(new Setting(key));
		if(index >= 0) {
			return listSettings.get(index);
			
		}
		
		return null;
	}
	
	//method for getting the value as string for a given key
	public String getValue(String key) {
		//returns the setting object
		Setting setting = get(key);
		
		//check if setting is not null
		if(setting != null) {
			return setting.getValue();
		}
		
		return null;
	}
	
	//method that update setting given key and value
	public void update(String key, String value) {
		//first get setting object
		Setting setting = get(key);
		//check if setting object and value object is not null
		if(setting != null && value != null) {
			//set setting the value
			setting.setValue(value);
		}
	}
	
	//method that return list of setting
	public List<Setting> list(){
		return listSettings;
	}
}
