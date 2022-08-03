package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE )
@Rollback(false)
public class CustomerRepositoryTest {

	@Autowired TestEntityManager entityManager;
	
	@Autowired CustomerRepository repo;
	
	
	@Test
	public void testCreateCustomer() {
		//select country by id
		Integer countryId = 234;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Peter");
		customer.setLastName("Mensah");
		customer.setEmail("peter@gmail.com");
		customer.setPassword("123");
		customer.setPhoneNumber("123456789");
		customer.setAddressLine1("P.O.Box 123");
		customer.setCity("Louisiana");
		customer.setState("Califonia");
		customer.setPostalCode("2345");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer); 
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testCreateCustomer2() {
		
		//grab country by id
		Integer countryId = 106;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer2 = new Customer();
		customer2.setFirstName("James");
		customer2.setLastName("Owusu");
		customer2.setEmail("james@gmail.com");
		customer2.setPassword("james123");
		customer2.setCountry(country);
		customer2.setState("Rajasthan");
		customer2.setCity("sahumai");
		customer2.setAddressLine1("254 Bachuma");	
		customer2.setAddressLine2("561 Est. Matiahi");
		customer2.setPhoneNumber("234567890");
		customer2.setPostalCode("2305");
		customer2.setCreatedTime(new Date());
		
		Customer savedCustomer2 = repo.save(customer2);
		
		assertThat(savedCustomer2).isNotNull();
		assertThat(savedCustomer2.getId()).isGreaterThan(0);
	
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(customer -> System.out.println(customer));		
		
		assertThat(customers).hasSizeGreaterThan(1);
		
	}
	
	@Test
	public void testUpdateCustomer() {
		//grab id of the customer to be updated
		Integer customerId = 2;
		Customer customer = repo.findById(customerId).get();
		
		String firstName = "Peter Kwadwo";
		customer.setFirstName(firstName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repo.save(customer);
		assertThat(updatedCustomer.getFirstName()).isEqualTo(firstName);
	
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 3;
		Optional<Customer> findCustomerById = repo.findById(customerId);
	
		assertThat(findCustomerById).isPresent();
		System.out.println(findCustomerById);
	
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 4;
		repo.deleteById(customerId);
		
		Optional<Customer> customer = repo.findById(customerId);
 		assertThat(customer).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email = "james@gmail.com";
		Customer customer = repo.findCustomerByEmail(email);
		
		assertThat(customer).isNotNull();
	}
	
	
//	@Test
//	public void testEnableCustomer() {
//		Integer customerId = 5;
//		repo.updateCustomerStatus(customerId, true);
//	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "code_123";
		Customer vCode =  repo.findByVerificationCode(code);
		
		assertThat(vCode).isNotNull();
		System.out.println(vCode);
	}
	
	//authentication type testing
	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 1;
		repo.updateAuthenticationType( id, AuthenticationType.FACEBOOK);
		
		Customer customer = repo.findById(id).get();
		
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
	}
}
