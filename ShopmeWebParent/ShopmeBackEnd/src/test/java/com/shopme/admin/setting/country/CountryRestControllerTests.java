package com.shopme.admin.setting.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc

public class CountryRestControllerTests {

	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@Autowired CountryRepository repo;

	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2021", roles = "ADMIN" )	
	public void testListCountries() throws Exception {
		
		//defines the countries url
		String url = "/countries/list";
		
		//convert list of countries from java to json
		MvcResult result = mockMvc.perform(get(url))
		.andExpect(status().isOk())
		.andDo(print())//print the response and details in the console
		.andReturn(); // return the result
		
		//convert the results which is in json to java
		//get the body content response as a string
	 	String jsonResponse = result.getResponse().getContentAsString();
	 	//System.out.println(jsonResponse);
	 	
	 	//read the json response into java object by using objectMapper
	 	
	    Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
	    
		/*
		 * for(Country country : countries) { System.out.println(country); }
		 */
		
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	
	//test create country
	@Test
	@WithMockUser(username = "nam@javacode.net", password = "something", roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		//define the url
		String url = "/countries/save";
		
		//define country name
		String countryName = "ITALY";
		String countryCode = "ITY";
		//creates new country object
		Country country = new Country(countryName, countryCode);	
		
		//make a request to the sever using mockMvc
	    MvcResult result = mockMvc.perform(post(url).contentType("application/json")
	    		.with(csrf())
				//write the java object (country) to json format using objectMapper class
				.content(objectMapper.writeValueAsString(country)))
			//prints the details of request and response
		.andDo(print())
		//server status
		.andExpect(status().isOk())
		.andReturn();
		
		String response = result.getResponse().getContentAsString();
		
		//cast country id from string to integer
		Integer countryId = Integer.parseInt(response);
		
		//find country by id
		Optional<Country> findById = repo.findById(countryId);
		
		//check the present of country id
		assertThat(findById.isPresent());		
		//saved country
		Country savedCountry = findById.get();
		
		assertThat(savedCountry.getName()).isEqualTo(countryName);
		
		System.out.println("Country ID: " + response);
		
		}
	
	
	//test update country
		@Test
		@WithMockUser(username = "nam@javacode.net", password = "something", roles = "ADMIN")
		public void testUpdateCountry() throws JsonProcessingException, Exception {
			//define the url
			String url = "/countries/save";
			
			//instantiate country object variables
			Integer countryId = 7;
			String countryName = "JAPAN";
			String countryCode = "JPN";
			//creates new country object
			Country country = new Country(countryId, countryName, countryCode);	
			
			//make a request to the sever using mockMvc
		    mockMvc.perform(post(url).contentType("application/json")
		    		.with(csrf())
					//write the java object (country) to json format using objectMapper class
					.content(objectMapper.writeValueAsString(country)))
				//prints the details of request and response
			.andDo(print())
			//server status
			.andExpect(status().isOk())
			.andExpect(content().string(String.valueOf(countryId)));
			
			
			
			//find country by id
			Optional<Country> findById = repo.findById(countryId);
			
			//check the present of country id
			assertThat(findById.isPresent());		
			//saved country
			Country savedCountry = findById.get();
			
			assertThat(savedCountry.getName()).isEqualTo(countryName);
			
			
			
			}
	
		//test delete country by id
		@Test
		@WithMockUser(username = "nam@javacode.net", password = "something", roles = "ADMIN")
		public void testDeleteCountry() throws Exception {
			Integer countryId = 7;
			String url = "/countries/delete/" + countryId;
			mockMvc.perform(get(url))
				.andExpect(status().isOk());
		
			Optional<Country> findById = repo.findById(countryId);
			
			assertThat(findById).isNotPresent();
			
		}
	
}
