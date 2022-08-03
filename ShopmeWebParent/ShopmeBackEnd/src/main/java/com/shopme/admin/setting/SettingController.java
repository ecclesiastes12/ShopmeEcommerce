package com.shopme.admin.setting;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;

@Controller
public class SettingController {

	@Autowired private SettingService service;
	@Autowired private CurrencyRepository currencyRepo;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		
		//model.addAttribute("listSettings", listSettings);
		model.addAttribute("listCurrencies", listCurrencies);
		
		//iterate through each setting 
		for (Setting setting : listSettings) {
			//write key and value of each setting to the model individually
			model.addAttribute(setting.getKey(), setting.getValue());		
			
		}
		
		return "settings/settings";
	}
	
	/*
	 * NB fileImage used in the request param paramter is the name attribute value
	 * of name of site logo in general.html
	 * 
	 * HttpServletRequest is used here is to read the field values of the form general.html.
	 * This is mainly because the form does not use objects like th:name="*{}"
	 */
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
		
		//grabs the general settings and assign it to the SettingBag object
		GeneralSettingBag settingBag = service.getGeneralSettings();
		
		saveSiteLogo(multipartFile, settingBag);//method call
		saveCurrencySymbol(request, settingBag);//method call
		
		updateSettingValuesFromForm(request, settingBag.list());
		
		ra.addFlashAttribute("message", "General settings have been saved.");
		
		return "redirect:/settings";
	}

	//private method that save site logo
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		//checks if multipartFile is not empty
		if (!multipartFile.isEmpty()) {
			//gets the file name
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			//set logo image path
			String value = "/site-logo/" + fileName;
			
			//update the value for the site logo
			settingBag.updateSiteLogo(value);
			
			//create file upload directory
			String uploadDir = "../site-logo/"; 
			
			//cleans the directory before file upload
			FileUploadUtil.cleanDir(uploadDir); // method call
			
			//save the image file
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); //method call
			
		}
	}
	
	//method for saving the currency symbol
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		//grab the currencyId from the form and cast it into integer
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		
		//finds currency by id from the db
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);
		
		//checks if the currencyiId exist
		if (findByIdResult.isPresent()) {
			//get the currency object by id
			Currency currency = findByIdResult.get();
			
			//update currency symbol
			settingBag.updateCurrencySymbol(currency.getSymbol());
			
		}
	}
	
	//method to update settings values
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSetting) {
		//loops through the list of setting
		for(Setting setting : listSetting) {
			/*
			 * grab the value(key) from the request parameter thus settings key.
			 * This is because on the form we used the name for the fields as key.
			 * eg.name="SITE_NAME",name="DECIMAL_DIGITS"
			 */
			String value = request.getParameter(setting.getKey());
			
			//check if the value of the key is not null
			if(value != null) {
				
				//update settings by setting value for the key
				setting.setValue(value);;
			}
		}
		
		//save all the list setting object
		service.saveAll(listSetting);
	}
	
	
	//handler method that save mail sever settings
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra) {
		
		//get the list of mail server settings
		List<Setting> mailServerSettings = service.getMailServerSettings();
		
		//update settings value from the form
		updateSettingValuesFromForm(request, mailServerSettings);
		
		ra.addFlashAttribute("message", "Mail server settings have been saved");
		
		return "redirect:/settings#mailServer";
	}
	
	//handler method that save mail template settings
		@PostMapping("/settings/save_mail_templates")
		public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra) {
			
			//get the list of mail server settings
			List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
			
			//update settings value from the form
			updateSettingValuesFromForm(request, mailTemplateSettings);
			
			ra.addFlashAttribute("message", "Mail template settings have been saved");
			
			return "redirect:/settings#mailTemplates";
		}
		
	//handler method that save payment settings
	@PostMapping("/settings/save_payment")
	public String savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
		//get the list of mail server settings
		List<Setting> paymentSettings = service.getPaymentSettings();
		
		//update settings value from the form
		updateSettingValuesFromForm(request, paymentSettings);
		
		ra.addFlashAttribute("message", "Payment settings have been saved");	
		
		return "redirect:/settings#payment";
	}
	
			
}
