package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@Service
public class ShoppingCartService {

	@Autowired CartItemRepository cartRepo;
	
	//method to update product quantity in the shopping cart
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
}
