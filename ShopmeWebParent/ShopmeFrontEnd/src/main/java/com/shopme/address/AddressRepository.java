package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;


public interface AddressRepository extends CrudRepository<Address, Integer>{

	List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT ad FROM Address ad WHERE ad.id = ?1 AND ad.customer.id = ?2")
	Address findByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("DELETE FROM Address ad WHERE ad.id = ?1 AND ad.customer.id = ?2")
	@Modifying
	void deleteByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false "
			+ "WHERE a.id != ?1 AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);
	
	//method that find default address of the customer. this is for checkout
	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
	public Address findDefaultByCustomer(Integer customerId );
	
	
}
