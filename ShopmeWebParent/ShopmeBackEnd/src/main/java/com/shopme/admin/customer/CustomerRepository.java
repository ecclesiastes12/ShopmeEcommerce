 package com.shopme.admin.customer;

 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {

	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
			+ "c.city, ' ', c.state,' ', c.country.name) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
	
	//find customer by email
//	@Query("SELECT c FROM Customer c WHERE c.email = :email")
//	public Customer findCustomerByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findCustomerByEmail(String email);

	
	
	//method to be used to check the existence of  customer before deletion
	public Long countById(Integer id);
	
	//method that enabled and disabled customer status
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateCustomerEnabledStatus(Integer id, boolean enabled);
	
	
}
