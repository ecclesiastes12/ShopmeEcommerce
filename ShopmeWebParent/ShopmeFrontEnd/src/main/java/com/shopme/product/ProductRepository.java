package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	//query to list enabled product in a specific category and in ascending order
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId,String categoryIDMatch, Pageable pageable);
	
	
	//method that find product by alias
	public Product findByAlias(String alias);
	
	
	//search product. First create search index of type full text in the database using the field name, short_descrption and full_description
	//NB nativeQuery is used here because full_text is specific to mysql db
	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
			+ "MATCH(name, short_description, full_description) AGAINST (?1)", 
			nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);


}
