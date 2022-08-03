package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

// NB this class represent currency settings

public class CurrencySettingBag extends SettingBag{

	//constructor that text a list of settings
	public CurrencySettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	
	//getter methods for currency settings
	//method that get currency symbol
	public String getSymbol() {
		return super.getValue("CURRENCY_SYMBOL");
	}
	
	//get symbol position
	public String getSymbolPostion() {
		return super.getValue("CURRENCY_SYMBOL_POSITION");
	}
	
	//get currency decimal point type
	public String getDecimalPointType() {
		return super.getValue("DECIMAL_POINT_TYPE");
	}
	
	//get thousands point type
	public String getThousandsPointType() {
		return super.getValue("THOUSANDS_POINT_TYPE");
	}
	
	//get decimal digits
	public int getDecimalDigits() {
		return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
	}
	
}
