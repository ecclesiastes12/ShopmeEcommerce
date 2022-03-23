package com.shopme.admin.customer;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.xpath.operations.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;


@Service
@Transactional
public class CustomerService {

	public static final int CUSTOMER_PER_PAGE = 10;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	public List<Customer> listAll(){
		return (List<Customer>) customerRepo.findAll();
	}
	
	//NB Check CustomerService1 for the previous code 
	
	//method that list customers by page updated with customerId for search purpose
	//listByPage method return type change to void because helper.updateModelAttributes(pageNum, page);
	//was moved from CustomerController class to CustomerService class
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		
		//method call
		helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
	}
		
		//method that get customer by email
		public Customer getCustomerEmail(String email) {
			return customerRepo.findCustomerByEmail(email);
		}
		
		//method to update customer status
		public void updateCustomerStatus(Integer id, boolean enabled) {
			customerRepo.updateCustomerEnabledStatus(id, enabled);
		}
		
		//method that list all countries
//		public List<Country> listAllCountries(){
//			return countrycustomerRepository.findAllByOrderByNameAsc();
//		}
		
		
		//method to get customer by id
		public Customer getCustomerId(Integer id) throws CustomerNotFoundException {
			try {
				return customerRepo.findById(id).get();
			} catch (Exception e) {
				throw new CustomerNotFoundException("Could not find any customers with ID " + id);
			}
		}
		
		//method that check customer email
		public boolean isEmailUnique(Integer id, String email) {
			//get customers email
			Customer existCustomer = customerRepo.findCustomerByEmail(email);
			
			//check customer id and email
			if(existCustomer != null && existCustomer.getId() != id) {
				// found another customer having the same email
				return false;
			}
			
			return true;
		}
		
//		//method that update customer
//		public void save(Customer customerInForm) {
//			
//			//grab existing password from the db using customer id if password field is empty
//			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
//			
//			//checks if customer password field is not empty
//			if(!customerInForm.getPassword().isEmpty()) {
//				//encrypts the password
//				String encodedPassword = encoder.encode(customerInForm.getPassword());
//				//sets the new password
//				customerInForm.setPassword(encodedPassword);
//			}else {
//				
//				//sets the new password
//				customerInForm.setPassword(customerInDB.getPassword());
//			}
//			
//			//set customer status, registration date and time,verification code  
//			//and authentication type by using the one that already exist in the db
//			customerInForm.setEnabled(customerInDB.isEnabled());
//			customerInForm.setCreatedTime(customerInDB.getCreatedTime());
//			customerInForm.setVerificationCode(customerInDB.getVerificationCode());
//			customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
//			//saves customer
//			customerRepo.save(customerInForm);
//		}
		
		
		//method that update customer modified with reset password token
		public void save(Customer customerInForm) {
			
			//grab existing password from the db using customer id if password field is empty
			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
			
			//checks if customer password field is not empty
			if(!customerInForm.getPassword().isEmpty()) {
				//encrypts the password
				String encodedPassword = encoder.encode(customerInForm.getPassword());
				//sets the new password
				customerInForm.setPassword(encodedPassword);
			}else {
				
				//sets the new password
				customerInForm.setPassword(customerInDB.getPassword());
			}
			
			//set customer status, registration date and time,verification code  
			//and authentication type by using the one that already exist in the db
			customerInForm.setEnabled(customerInDB.isEnabled());
			customerInForm.setCreatedTime(customerInDB.getCreatedTime());
			customerInForm.setVerificationCode(customerInDB.getVerificationCode());
			customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
			
			customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
			//saves customer
			customerRepo.save(customerInForm);
		}
		//method that list all countries. for customer edit form
		public List<Country> listAllCountries(){
			return countryRepository.findAllByOrderByNameAsc();
		}
		
		public void delete(Integer id) throws CustomerNotFoundException {
			Long count = customerRepo.countById(id);
			if (count == null || count == 0) {
				throw new CustomerNotFoundException("Could not find any customers with ID " + id);
			}
			
			customerRepo.deleteById(id);
		}
}
