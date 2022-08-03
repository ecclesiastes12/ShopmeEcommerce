package com.shopme.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	Customer findCustomerByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	Customer findByVerificationCode(String code);
	
	
	//this also works
	//method to enable and disabled customer status
//	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
//	@Modifying
//	void updateCustomerStatus(Integer id, boolean enabled);
	
	//method to enable and disabled customer status
//	@Query("UPDATE Customer c SET c.enabled = true WHERE c.id = ?1")
//	@Modifying
//	public void enable(Integer id);	
		
	//method modified
	//method to enable and disabled customer status by id
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);	
	
	//without @Modifying update query will not work
	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying 
	public void updateAuthenticationType(Integer customerId, AuthenticationType type);
	
	//for reset password
	public Customer findByResetPasswordToken(String token);
}
