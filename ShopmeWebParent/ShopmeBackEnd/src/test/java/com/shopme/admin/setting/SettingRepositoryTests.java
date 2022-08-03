package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

	
	@Autowired
	SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		//NB check how the enum variable is select SettingCategory.GENERAL 
		//Setting siteName = new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		
		Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopme Ltd.", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteLogo,copyright));
		
		//find all by iteration
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);		
	
	}

	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
		Setting decimalPointTyoe = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointTyoe, decimalDigits, thousandsPointType));		
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.PAYMENT);
		
		settings.forEach(System.out::println);
		}
	
	//test method add paypal api client secrte 
			@Test
			public void testAddPaymentSettingsPaypalApiBaseUrl() {
				String key = "PAYPAL_API_BASE_URL";
				String value = "https://api-m.sandbox.paypal.com";
				
				Setting setting = new Setting();
				setting.setKey(key);
				setting.setValue(value);
				setting.setCategory(SettingCategory.PAYMENT);
				
				Setting savedSetting = repo.save(setting);
				
				assertThat(savedSetting).isNotNull();
				assertThat(savedSetting.getKey()).hasSizeGreaterThan(0);
			}
			
	
	//test method add paypal api client id 
		@Test
		public void testAddPaymentSettingsPayPalClientId() {
			String key = "PAYPAL_API_CLIENT_ID";
			String value = "PAYPAL_CLIENT_ID";
			
			Setting setting = new Setting();
			setting.setKey(key);
			setting.setValue(value);
			setting.setCategory(SettingCategory.PAYMENT);
			
			Setting savedSetting = repo.save(setting);
			
			assertThat(savedSetting).isNotNull();
			assertThat(savedSetting.getKey()).hasSizeGreaterThan(0);
		}
		
		//test method add paypal api client secrte 
		@Test
		public void testAddPaymentSettingsPaypalClientSecret() {
			String key = "PAYPAL_API_CLIENT_SECRET";
			String value = "PAYPAL_CLIENT_SECRET";
			
			Setting setting = new Setting();
			setting.setKey(key);
			setting.setValue(value);
			setting.setCategory(SettingCategory.PAYMENT);
			
			Setting savedSetting = repo.save(setting);
			
			assertThat(savedSetting).isNotNull();
			assertThat(savedSetting.getKey()).hasSizeGreaterThan(0);
		}
		
		//test method add order confirmation subject
		@Test
		public void testConfirmOrderSubject() {
			String key = "CONFIRM_ORDER_SUBJECT";
			String value = "confirmation of your order ID";
			
			Setting setting = new Setting();
			setting.setKey(key);
			setting.setValue(value);
			setting.setCategory(SettingCategory.PAYMENT);
			
			Setting savedSetting = repo.save(setting);
			
			assertThat(savedSetting).isNotNull();
			assertThat(savedSetting.getKey()).hasSizeGreaterThan(0);
		}
		
		//test method add order confirmation content 
		@Test
		public void testAddConfirmOrderContent() {
			String key = "CONFIRM_ORDER_CONTENT";
			String value = "This email is to confirm";
			
			Setting setting = new Setting();
			setting.setKey(key);
			setting.setValue(value);
			setting.setCategory(SettingCategory.PAYMENT);
			
			Setting savedSetting = repo.save(setting);
			
			assertThat(savedSetting).isNotNull();
			assertThat(savedSetting.getKey()).hasSizeGreaterThan(0);
		}


}
