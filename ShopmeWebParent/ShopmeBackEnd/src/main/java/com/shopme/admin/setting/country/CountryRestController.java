package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;

@RestController
public class CountryRestController {

	@Autowired
	CountryRepository repo;
	
	//method that list all countries from the db
	@GetMapping("/countries/list")
	public List<Country> listAll(){
		
		return repo.findAllByOrderByNameAsc();//method call  
	}
	
	//method that makes changes to country in the db
	//NB @RequestBody is used here so that spring can convert json string to country object in java
	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		
		//save country
		Country saveCountry = repo.save(country);
		
		//return the id of saved or updated country object
		return String.valueOf(saveCountry.getId());
	}
	
	//delete country by id
	/*
	 * @GetMapping("/countries/delete/{id}") public void delete(@PathVariable("id")
	 * Integer id) { repo.deleteById(id); }
	 */
	
	//modified 
	//delete country by id
		@DeleteMapping("/countries/delete/{id}")
		public void delete(@PathVariable("id") Integer id) {
			repo.deleteById(id);
		}
}
