package com.shopme.admin.shippingrate;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.State;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer>{

	//method that find rate by country and state
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state = ?2")
	ShippingRate findByCountryAndState(Integer country, String state);
	
	//method that update CODSupport
	@Query("UPDATE ShippingRate sr SET sr.codSupported = ?1 WHERE sr.id = ?2")
	@Modifying
	void updateCODSupport(boolean enabled, Integer rateId);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
}
