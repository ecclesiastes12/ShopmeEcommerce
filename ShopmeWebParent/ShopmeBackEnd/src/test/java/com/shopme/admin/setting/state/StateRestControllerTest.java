package com.shopme.admin.setting.state;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired CountryRepository countryRepo;
	@Autowired StateRepository stateRepo;
	
	
	@Test
	@WithMockUser(username = "nam@codejava.net", password = "nam2021", roles = "ADMIN" )	
	public void testListStateByCountryId() throws Exception {
		
		Integer countryId = 1;
		
		String url = "/states/list_by_country/" + countryId;
		
		MvcResult result = mockMvc.perform(get(url))
							.andExpect(status().isOk())
							.andDo(print())
							.andReturn();
		
		//convert the results which is in json to java
		//get the body content response as a string
		String jsonResponse = result.getResponse().getContentAsString();
		
		State[] state = objectMapper.readValue(jsonResponse, State[].class);
		
		assertThat(state).hasSizeGreaterThan(0);
		
	}
	
	@Test
	@WithMockUser(username = "nam@javacode.net", password = "something", roles = "ADMIN")
	public void testCreateStateByCountryId() throws Exception {
		String url = "/states/save";
		Integer countryId = 1;
		Country country = countryRepo.findById(countryId).get();
		State state = new State("Payota", country);
		
		MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json")
								.with(csrf())
								.content(objectMapper.writeValueAsString(state)))
								.andDo(print())
								.andExpect(status().isOk())
								.andReturn();
						
		String response = mvcResult.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);
		
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	@WithMockUser(username = "nam@javacode.net", password = "something", roles = "ADMIN")
	public void testUpdateState() throws Exception{
		String url = "/states/save";
		Integer stateId = 10;
		String stateName = "Payotama";
		
		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);
		
		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(stateId)));
		
		Optional<State> findStateById = stateRepo.findById(stateId);
		assertThat(findStateById.isPresent());
		
		State updateState = findStateById.get();
		assertThat(updateState.getName()).isEqualTo(stateName);
	
	}
	
}
