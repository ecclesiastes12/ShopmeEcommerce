package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;

public interface CartItemRepository extends CrudRepository<CartItem, Integer>{

	//method that return list of cart items by a customer
	List<CartItem> findByCustomer(Customer customer);
	
	//method that return a single cart item object based on customer and product
	CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	//method that update cart item quantity based on customer id and product id
	@Modifying  //@Modifying annotation is used because it's an update query
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	public void updateQuantity(Integer quantity, Integer customerId, Integer productId);
	 
	
	//method that delete cart item based on customer id and product id
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);

	//main purpose of this method is to empty cart when order is placed
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1")
	public void deleteByCustomer(Integer customerId);

}
