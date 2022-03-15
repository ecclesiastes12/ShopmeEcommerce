package com.shopme.admin.setting.state;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;

@RestController
public class StateRestController {

	@Autowired
	StateRepository repo;
	
	//method to list state by country id
	@GetMapping("/states/list_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
		List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));	
		
		List<StateDTO>	result = new ArrayList<>();
		
		for(State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}
		
		return result;
	}
	
	//method to save state
	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State savedState = repo.save(state); //save states
		return String.valueOf(savedState.getId()); //return id of savedState
	}
	
	
	//method that delete state by id
//	@GetMapping("/states/delete/{id}")
//	public void delete(@PathVariable("id") Integer id) {
//		repo.deleteById(id);
//	}
	
	//method that delete state by id modified
	@DeleteMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}