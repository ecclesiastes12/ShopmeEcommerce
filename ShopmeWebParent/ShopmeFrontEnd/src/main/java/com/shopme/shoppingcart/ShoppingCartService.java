package com.shopme.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired CartItemRepository cartRepo;
	@Autowired ProductRepository productRepo;
	
	//method to update product quantity in the shopping cart using the quantity control buttons from the product details page
 	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		
		//create product object based on product id
		Product product = new Product(productId);
		
		//finds cart item based on customer and product id
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		 
		//check if cart item is not null meaning product is already in the shopping cart
		if(cartItem != null) {
			//update product quantity in the cart item
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			//throws exception if quantity of a particular product added exceed 5
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add " + quantity + " more item(s)"
						+ " because there's already " + cartItem.getQuantity() + " item(s) "
								+ "in your shopping cart. Maximum allowd quantity is 5.");
			}
		}else {//in case product is not in the cart item
			//create new cart item object
			cartItem = new CartItem();
			
			//set customer object and product object for the new cart item
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
			
		}
		
		//set cart item quantity to updated quantity
		cartItem.setQuantity(updatedQuantity);
		
		//save the cart item
		cartRepo.save(cartItem);
			
		return updatedQuantity;
		
	}
	
	
	//method that list cart item(items in the cart)
	public List<CartItem> listCartItems(Customer customer){
		
		// returns list of items object associated to a given customer
		return cartRepo.findByCustomer(customer);
	}
	
	//method to update product quantity in the shopping cart using the quantity control buttons
	//and directly in the shopping cart
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		//method call
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		
		//get product object based on product id
		Product product = productRepo.findById(productId).get();
		
		//calculate new subtotal
		float subtotal = product.getDiscountPrice() * quantity;		
		
		//return new subtotal
		return subtotal;
	}
	
	//method that remove product from cart_item table in the database
	public void removeProduct(Integer productId, Customer customer) {
		cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
		
	}
	
	//method that empty cart when customer placed an order
	public void deleteByCustomer(Customer customer) {
		cartRepo.deleteByCustomer(customer.getId());
	}
}
