package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
public class CheckoutService {

	//NB this DIM_DIVISOR constant is used by fedex
	private static final int DIM_DIVISOR = 139;
	
	//method that prepares the checkout for customer ordered items
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		//creates checkoutinfo object
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		//calculate product cost which consist of list of cart items
		float productCost = calculateProductCost(cartItems);
		
		//calculate product total amount
		float productTotal = calculateProductTotal(cartItems);
		
		//calculate shipping cost total of products in cart items
		float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
		
		//calculate paymentTotal for products in cart
		float paymentTotal = productTotal + shippingCostTotal;
		
		//set product cost for checkout info
		checkoutInfo.setProductCost(productCost);
		
		//set product total for checkout info
		checkoutInfo.setProductTotal(productTotal);
		
		//set payment total
		checkoutInfo.setPaymentTotal(paymentTotal);
		
		//set deliver days for the product from the shipping rate object
		checkoutInfo.setDeliveryDays(shippingRate.getDays());
		
		//set cod supported for checkout info
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		
		//set shipping cost total for checkoutinfo
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		
		return checkoutInfo;
	}

	//method that calculate shipping cost of all product in cart
	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		
		//iterate through each cart item
		for(CartItem item : cartItems) {
			
			//get product object from the item
			Product product = item.getProduct();
			
			//calculate dimensional weight (DIM) by multiply L*W*H/DIM_DIVISOR of the package
			//that contain the product and not the product itself
			float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
			
			//calculate final weight.
			//it will used product weight if product weight > dimWeight else dimWeight will be used
			float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		
			//calculate shipping cost of product in cart item
			float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();
			
			//set shipping cost of each product in the cart item
			item.setShippingCost(shippingCost);
			
			shippingCostTotal += shippingCost;
		}
		
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
	float total = 0.0f;
		
		//iterate through each product in the cart
		for(CartItem item : cartItems) {
			
			//calculate product total in the cart by summing all the subtotal
			total += item.getSubtotal();
		}
		
		return total;
	}

	//method that calculate product cost
	private float calculateProductCost(List<CartItem> cartItems) {
		float cost = 0.0f;
		
		//iterate through each product in the cart
		for(CartItem item : cartItems) {
			
			//calculate cost base on quantity of items in the cart multiply by product cost
			cost += item.getQuantity() * item.getProduct().getCost();
		}
		
		return cost;
	}
	
}
