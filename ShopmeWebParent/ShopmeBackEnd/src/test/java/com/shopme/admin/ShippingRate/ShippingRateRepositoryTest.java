package com.shopme.admin.ShippingRate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.shippingrate.ShippingRateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.User;
import com.shopme.common.entity.product.Product;

@DataJpaTest(showSql= false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTest {

	@Autowired ShippingRateRepository repo;
	
	@Autowired TestEntityManager entityManager;
	
	//test method that create new shipping rate
	@Test
	public void testCreateNewShippingRate() {
		Integer countryId =234;
		Country country = new Country(countryId);
		Float rate = 2.57f;
		int days = 3;
		boolean codSupported = true;
		
		ShippingRate newRate = new ShippingRate();
		newRate.setCountry(country);
		newRate.setCodSupported(codSupported);
		newRate.setDays(days);
		newRate.setRate(rate);
		newRate.setState("New Jersey");
		
		ShippingRate savedRate = repo.save(newRate);
		
		assertThat(savedRate.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewShippingRate2() {
		Integer countryId =32;
		Country country = new Country(countryId);
		Float rate = 4.57f;
		int days = 5;
		boolean codSupported = true;
		
		ShippingRate newRate = new ShippingRate();
		newRate.setCountry(country);
		newRate.setCodSupported(codSupported);
		newRate.setDays(days);
		newRate.setRate(rate);
		newRate.setState("Espírito Santo");
		
		ShippingRate savedRate = repo.save(newRate);
		assertThat(savedRate.getId()).isNotNull();
		assertThat(savedRate.getId()).isGreaterThan(0);
	}
	
	//test method that update shipping rate
	@Test
	public void testUpdateShippingRate() {
		ShippingRate theRate = repo.findById(2).get();
		theRate.setState("Amazonas");
		repo.save(theRate);
	}
	
	//test method that list all shipping rate
	@Test
	public void testFindAllShipping() {
		Iterable<ShippingRate> listUsers = repo.findAll();
		listUsers.forEach(ShippingRate -> System.out.println(ShippingRate));
	}
	
	//test method that delete shipping rate
	@Test
	public void testDeleteShippingRate() {
		Integer rateId = 1;
		repo.deleteById(rateId);
		Optional<ShippingRate> result = repo.findById(rateId);
		//assertThat(result.isEmpty());
		assertThat(!result.isPresent());
		
	}
	
	//test method that find shipping rate by country and state
	@Test
	public void testFindByCountryAndState() {
		Integer countryId = 234;
		String state = "New Jersey";
		
		ShippingRate rate = repo.findByCountryAndState(countryId,state);
		assertThat(rate).isNotNull();
		System.out.println(rate);
	}
	
	//test method that find shipping rate by country and state
	@Test
	public void testUpdateCODSupport() {
		Integer rateId = 2;
		repo.updateCODSupport(false, rateId);
	}
}
