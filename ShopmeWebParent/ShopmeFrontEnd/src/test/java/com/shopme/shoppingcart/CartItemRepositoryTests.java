package com.shopme.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

	@Autowired CartItemRepository repo;
	@Autowired TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 1;
		Integer productId = 1;
		
		//get the customer and product object through the entity manager 
		//by customer id and product id
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		//create new cart item object
		CartItem newItem = new CartItem();  
		
		//set references of the customer and the product
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		
		//set the quantity products in the shopping cart
		newItem.setQuantity(1);
		
		//save the item in the shopping cart
		CartItem savedItem = repo.save(newItem);
		
		assertThat(savedItem.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSave2Item() {
		Integer customerId = 10;
		Integer productId = 10;
		
		//get the customer and product object through the entity manager 
		//by customer id and product id
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		//create new cart item object
		CartItem item1 = new CartItem();  
		
		//set references of the customer and the product
		item1.setCustomer(customer);
		item1.setProduct(product);
		
		//set the quantity products in the shopping cart
		item1.setQuantity(2);
		
		//create new cart item object
		CartItem item2 = new CartItem();  
		
		//set references of the customer and the product
		item2.setCustomer(new Customer(customerId)); //same customer 
		item2.setProduct(new Product(8)); //different product
		
		//set the quantity products in the shopping cart
		item2.setQuantity(3);


		//save all the item in the shopping cart
		Iterable<CartItem> iterable = repo.saveAll(List.of(item1, item2));
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	//test method that list cart items of the customer
	@Test
	public void testFindByCustomer() {
		Integer customerId = 10;
		
		//list cart items of the customer
		List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));
		
		listItems.forEach(System.out::println);
		
		assertThat(listItems.size()).isEqualTo(2); //meaning list of cart items of the customer is 2
	}
	
	//test method that return a single cart item object based on customer and product
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 10;
		Integer productId = 10;
	    CartItem item =	repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
	    
	    assertThat(item).isNotNull();
	    
	    System.out.println(item);
	}
	
	//method that update cart item quantity based on customer id and product id
	@Test
	public void testUpdateQuantity() {
		Integer customerId = 1;
		Integer productId = 1;
		Integer quantity = 4;
		
		//update cart item quantity
		repo.updateQuantity(quantity, customerId, productId);
		
		//get cart item based on customer id and product id
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item.getQuantity()).isEqualTo(4);
	}
	
	//test method that delete cart item based on customer id and product id
	@Test
	public void deleteByCustomerAndProduct() {
		Integer customerId = 10;
		Integer productId = 10;
		
		repo.deleteByCustomerAndProduct(customerId, productId);
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNull();
		
	}
}
