package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE )
@Rollback(false)
public class AddressRepositoryTest {

	@Autowired AddressRepository repo;
	@Autowired TestEntityManager entityManager;
	
	
	//test method to add new address
	@Test
	public void testAddNew() {
		//select country by id
		Integer countryId = 234;
		Country country = entityManager.find(Country.class, countryId);
		
		Integer customerId = 52;
		Customer customer = entityManager.find(Customer.class, customerId);
		
		Address address = new Address();
		address.setCountry(country);
		address.setCustomer(customer);
		address.setFirstName("Samuel");
		address.setLastName("Okyere");
	
		address.setPhoneNumber("0345156789");
		address.setAddressLine1("P.O.Box St. Louis 234");
		address.setCity("Louisiana");
		address.setState("Califonia");
		address.setPostalCode("2345");
		address.setDefaultForShipping(false);
		
		Address savedAddress = repo.save(address);
		
		assertThat(savedAddress.getId()).isNotNull();
		
	}
	
	@Test
	public void testAddNew2() {
		//select country by id
		Integer countryId = 83;
		Country country = entityManager.find(Country.class, countryId);
		
		Integer customerId = 54;
		Customer customer = entityManager.find(Customer.class, customerId);
		
		Address address = new Address();
		address.setCountry(country);
		address.setCustomer(customer);
		address.setFirstName("Solomon");
		address.setLastName("Kwabina Otoo");
	
		address.setPhoneNumber("0345783213");
		address.setAddressLine1("P.O.Box 23");
		address.setCity("Accra");
		address.setState("Ghana");
		address.setPostalCode("2345");
		address.setDefaultForShipping(true);
		
		Address savedAddress = repo.save(address);
		
		assertThat(savedAddress.getId()).isNotNull();	
	}
	
	//test that find by customer
	@Test
	public void testFindByCustomer() {
		Integer customerId = 54;
		
	 	List<Address> listAddresses =  repo.findByCustomer(new Customer(customerId));
	 	assertThat(listAddresses.size()).isGreaterThan(0);
	 	
	 	listAddresses.forEach(System.out::println);
	}
	
	//test method that find by address id and customer id
	@Test
	public void testFindByIdAndCustomer() {
		Integer addressId = 2;
		Integer customerId = 54;
		//Customer customer = entityManager.find(Customer.class, customerId);
		Address savedAddress = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(savedAddress).isNotNull();
		
		System.out.println(savedAddress);		
	}
	
	//test method that update address
	@Test
	public void updateAddress() {
		Integer addressId = 1;
		String phoneNumber = "0243567234";
		
		Address address = repo.findById(addressId).get();
		
		address.setPhoneNumber(phoneNumber);
		address.setDefaultForShipping(true);
		Address updateAddress = repo.save(address);
		
		assertThat(updateAddress.getPhoneNumber()).isEqualTo(phoneNumber);
		
	}
	
	//test method that delete address by address id and customer id
	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 2;
		Integer customerId = 52;
		
		repo.deleteByIdAndCustomer(addressId, customerId);
		
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNull();
		
	}
	
	//test method to set default address
	@Test
	public void testSetDefault() {
		Integer addressId = 8;
		repo.setDefaultAddress(addressId);
	
		Address address = repo.findById(addressId).get();
		
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	//test method to set default address
	@Test
	public void testSetNonDefaultAddress() {
		Integer addressId = 8;
		Integer customerId = 52;
		repo.setNonDefaultForOthers(addressId, customerId);
	}
	
	//test method that find the default address of the customer
	@Test
	public void testGetDefualt() {
		Integer customerId = 5;
		Address address = repo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
	}
}
