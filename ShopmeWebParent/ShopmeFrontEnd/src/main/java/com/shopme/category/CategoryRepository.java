package com.shopme.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	//query that list enabled categories in ascending order
	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	List<Category> findAllEnabled();
	
	//query that finds category by alias that is enabled
	@Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias = ?1")
	Category findByAliasEnabled(String alias);
}
