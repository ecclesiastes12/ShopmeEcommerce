package com.shopme.admin.setting;

import java.util.List;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

public class GeneralSettingBag extends SettingBag {

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
		
	}
	/*
	 * NB. on the general setting.html form there's no field for currency symbol. 
	 *     We only have CURRENCY_ID and currency symbol is retrieved base on CURRENCY_ID.
	 *     updateCurrencySymbol method will be used to update the currency symbol
	 */
	//method for updating currency symbol
	public void updateCurrencySymbol(String value) {
		//CURRENCY_SYMBOL is a key and is from the db
		super.update("CURRENCY_SYMBOL", value);
	}
	
	//In setting.html we only have file input field for site log
	//method for updating site logo
	public void updateSiteLogo(String value) {
		//SITE_LOGO is a key and is from the db
		super.update("SITE_LOGO", value);
		
	}
	
}
