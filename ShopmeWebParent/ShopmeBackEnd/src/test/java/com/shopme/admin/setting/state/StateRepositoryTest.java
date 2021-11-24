package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTest {

	@Autowired
	StateRepository repo;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInUs() {
		
		Integer countryId = 3;
		Country country = entityManager.find(Country.class, countryId);
		
		State state = repo.save(new State("Washington", country));
		//State state = repo.save(new State("California", country));
		//State state = repo.save(new State("Texas", country));
		//State state = repo.save(new State("New York", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStatesInIndia() {
		
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);
		
		//State stateInIndia1 = repo.save(new State("Karnataka", country));
		//State stateInIndia2 = repo.save(new State("Punjab", country));
		State stateInIndia3 = repo.save(new State("Texas", country));
		State stateInIndia4 = repo.save(new State("New York", country));
		
		//assertThat(state).isNotNull();
		//assertThat(state.getId()).isGreaterThan(0);
		repo.saveAll(List.of(stateInIndia3,stateInIndia4));
	}
	
	//list states by country
	@Test
	public void testListStatesByCountry() {
		
		Integer countryId = 1;
		Country country = entityManager.find(Country.class, countryId);
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	//updates states
	@Test
	public void testUpdateState() {
		Integer stateId = 1;
		String stateName = "West Bengal india";
		State states = repo.findById(stateId).get();
		
		states.setName(stateName);
		State updateState = repo.save(states);
		
		assertThat(updateState.getName()).isEqualTo(stateName);
	}
	
	//get state or check if a state is present
	@Test
	public void testGetState() {
		Integer stateId = 2;
		Optional<State> findById = repo.findById(stateId);
		
		assertThat(findById.isPresent());
	}
	
	//delete state 
	@Test
	public void testDeleteState() {
		Integer stateId = 8;
		repo.deleteById(stateId);
		
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isEmpty());
	}
}
