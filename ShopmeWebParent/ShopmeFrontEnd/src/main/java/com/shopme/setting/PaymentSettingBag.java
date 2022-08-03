package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

public class PaymentSettingBag extends SettingBag{

	//constructor that text list of  object
	public PaymentSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	//getter methods that return the values of paypal url ,client id and client secret code
	public String getUrl() {
		return super.getValue("PAYPAL_API_BASE_URL");
	}
	
	public String getClientID() {
		return super.getValue("PAYPAL_API_CLIENT_ID");
	}
	
	public String getClientSecret() {
		return super.getValue("PAYPAL_API_CLIENT_SECRET");
	}
	
}
