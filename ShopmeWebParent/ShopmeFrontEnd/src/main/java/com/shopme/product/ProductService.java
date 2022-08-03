package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {

	//static variable to list product per page
	public static final int PRODUCT_PER_PAGE = 10;
	public static final int SEARCH_RESULTS_PER_PAGE = 10;
	
	@Autowired
	ProductRepository repo;
	
	//method to list product by category
	public Page<Product> listByCategory(int pageNum, Integer categoryId){
		
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
		
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
	//method that get product by alias
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		
		//checks if product is not null
		if(product == null) {
			throw new ProductNotFoundException("Could not find any product with alias " + alias);
		}
		
		return product;
	}
	
	
	//method that search products
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		
		return repo.search(keyword, pageable);
	}
	
}
