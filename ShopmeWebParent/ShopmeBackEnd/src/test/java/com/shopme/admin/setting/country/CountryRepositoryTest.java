package com.shopme.admin.setting.country;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {

	@Autowired
	CountryRepository repo;
	
	@Test
	public void testCreatCountry() {
		
		String countryName = "United States of America";
		String countryCode = "US";
		
		Country country = repo.save(new Country(countryName, countryCode));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
			
	}
	
	@Test
	public void testCreatCountry2() {
		
		Country countryGhana = repo.save(new Country("Ghana", "GH"));
		Country countryBritain = repo.save(new Country("United Kingdom", "BRT"));
		Country countryAustra = repo.save(new Country("Australia", "AUT"));
		Country countryGermany = repo.save(new Country("Germany", "GR"));
		repo.saveAll(List.of(countryGhana,countryBritain,countryAustra,countryGermany))	;		
	}
	
	@Test
	public void testListCountories() {
		List<Country> listCountries = repo.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
		
		assertThat(listCountries.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCountry() {
		Integer id = 1;
		String name = "China";
		Country country = repo.findById(id).get();
		
		country.setName(name);
		//country.setCode("CN");
		
		Country updateCountry = repo.save(country);
		assertThat(updateCountry.getName()).isEqualTo(name);
	}
	
	//get method 
	@Test
	public void testGetCountry() {
		Integer id = 3;
		
		Country country= repo.findById(id).get();
		assertThat(country).isNotNull();
	}
	
	//delete country method
	public void testDeleteCountry() {
		Integer id = 5;
		
		 repo.deleteById(id);
		
		Optional<Country> findById = repo.findById(id);
		assertThat(findById.isEmpty());
		}
}
