package com.shopme.admin.order;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.order.Order;

@Service
public class OrderService {

	@Autowired OrderRepository repo;
	
	//constant variable for page size thus total number of list per page
	public static final int ORDERS_PER_PAGE = 10;
	
//	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
//	//sort by field name
//	Sort sort = Sort.by(sortField);
//	
//	//sort in ascending or descending order
//	sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//	
//	Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
//	
//	//checks if the keyword is not null thus search parameter
//	if(keyword != null) {
//		return repo.findAll(keyword,pageable);
//	}
//	
//	return repo.findAll(pageable);
//}
	
	//listByPage method return type change to void because helper.updateModelAttributes(pageNum, page);
	//was moved from OrderController class to OrderService class
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		//check if sort field is destination
		if ("destination".equals(sortField)) {
			//sort destination field by country, state and city
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			//sort by field name
			 sort = Sort.by(sortField);
		}
	
		//sort in ascending or descending order
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;		
		
		//checks if the keyword is not null thus search parameter
		if(keyword != null) {
			page = repo.findAll(keyword,pageable);
		}else {
			page = repo.findAll(pageable);
		}
		
		helper.updateModelAttributes(pageNum, page);
	}
	
	//method that get order by id
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException  ex) {
			throw new OrderNotFoundException("Could not find order with ID: " + id);
		}
	}
	
	//method that delete order
	public void delete(Integer id) throws OrderNotFoundException {
		Long countById = repo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new OrderNotFoundException("Could not find order with ID: " + id);
		}
		
		repo.deleteById(id);
	}
	
	
	
}
