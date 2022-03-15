package com.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired private CountryRepository countryRepository;
	@Autowired private CustomerRepository customerRepo;
	@Autowired PasswordEncoder passwordEncoder;
	
	//method to return list of countries
	public List<Country> listAllCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	//method to check uniqueness of customer email
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findCustomerByEmail(email);
		
		return customer == null; //if null value is return it means email is unique
	}
	
	
	//method that register customer
	public void registerCustomer(Customer customer) {
		//method call
		encoderPassword(customer);//encode customer password
		customer.setEnabled(false);//disabled customer registed status
		customer.setCreatedTime(new Date()); //set customer registration time to the current time
	
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		
		//generation random string for verification code when the customer register
		String randomCode = RandomString.make(64); //make(64) sets the length of the random string
		
		//System.out.println("Verification Code: " + randomCode);
		
		//sets the random String as verification code
		customer.setVerificationCode(randomCode);
		
		
		customerRepo.save(customer);
	}
	
	//method that find customer by email. this method is meant for OAuth2loginSuccessHandler
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findCustomerByEmail(email);
	}

	//method that encrypts customer's password
	private void encoderPassword(Customer customer) {
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		
		//set encoded password back to the customer object
		customer.setPassword(encodePassword);
	}
	
	//method that verify customers account
	public boolean verify(String verificationCode) {
		//grab customer object by verification code
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
		
		//check if customer is null or enabled
		if (customer == null || customer.isEnabled()) {
			return false;
			
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}
	
	//method that add new customer via OAuth2 login if the customer does not exist in the db
			public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
				//creates new customer
				Customer customer = new Customer();
				
				//set customer's email
				customer.setEmail(email);
				
				//method call
				setName(name, customer);
				
				customer.setEnabled(true);
				customer.setCreatedTime(new Date());
				//sets the authentication type to google if customer login with google account
				customer.setAuthenticationType(AuthenticationType.GOOGLE);
				
				//NB if customer login with google account there is no need to set password for the customer
				customer.setPassword("");
				customer.setAddressLine1("");
				customer.setCity("");
				customer.setState("");
				customer.setPhoneNumber("");
				customer.setPostalCode("");
				customer.setCountry(countryRepository.findByCode(countryCode));
				
				customerRepo.save(customer);
				
				
			}
			
			//method that set name based on getName method  in CustomerOAuth2User class
			private void setName(String name, Customer customer) {
				
				//split the name
				String[] nameArray = name.split(" ");
				
				//check if nameArray length is less than 2, it means it doesn't contain firstName and lastName
				if(nameArray.length < 2) {
					customer.setFirstName(name);
					customer.setLastName(""); //set lastName to empty string if array length is less than 2
				}else {
					//assign the first element in the array to firstName
					String firstName = nameArray[0];
					//set firstName
					customer.setFirstName(firstName);
					
					//replace the first name with an empty string to get the last name
					String lastName = name.replaceFirst(firstName, "");
					//set lastName
					customer.setLastName(lastName);
				}
			}
}


