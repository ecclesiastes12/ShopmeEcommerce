package com.shopme.address;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;
import com.shopme.setting.CountryRepository;

@Service
@Transactional
public class AddressService {

	@Autowired AddressRepository repo;
	@Autowired CountryRepository countryRepository;
	@Autowired CustomerRepository customerRepository;
	
	public List<Address> listAddressBook(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	
	//method that create new address book
	public void saveAddress(Address address) {	
		repo.save(address);
	}
	
	//method that get address by id
	public Address getAddress(Integer addressId, Integer customerId) {
		return repo.findByIdAndCustomer(addressId, customerId);
	}
	
	//method to delete address
	public void deleteAddress(Integer addressId, Integer customerId) {
		repo.deleteByIdAndCustomer(addressId, customerId);
	}
	
	//method that set default address for customer
	public void setDefaultAddress(Integer defaultAddressId, Integer CustomerId) {
		//check if default address id is greater than 0
		if (defaultAddressId > 0) {
			//method call
			//set default address for customer
			repo.setDefaultAddress(defaultAddressId);
		}
		//set other other addresses of the some to non default if any
		repo.setNonDefaultForOthers(defaultAddressId, CustomerId);
	}
	
	//method that get the default address of the customer if default address of customer is supported
	public Address getDefaultAddress(Customer customer) {
		return repo.findDefaultByCustomer(customer.getId());
	}
}
