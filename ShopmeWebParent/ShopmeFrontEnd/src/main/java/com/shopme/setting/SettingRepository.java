package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String>{

	//method that return list of setting object by setting category
	public List<Setting> findByCategory(SettingCategory category);
	
	//method that return General and Currency categories from the settings table
	@Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category =?2")
	public List<Setting> findByTwoCategories(SettingCategory catOne, SettingCategory catTwo);
	
	//method that find settings by key. this is related to payment  
	public Setting findByKey(String key);
}
