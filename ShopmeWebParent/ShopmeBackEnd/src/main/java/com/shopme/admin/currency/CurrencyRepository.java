package com.shopme.admin.currency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

	//list the currency by name and in ascending order
	public List<Currency> findAllByOrderByNameAsc();
	
}
