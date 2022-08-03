package com.shopme.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@Service
public class OrderService {

	
	/*
	 * NB creating an order will include the following:
	 * 1.customer object
	 * 2.customer address
	 * 3.list of items ordered thus cart items
	 * 4.payment method
	 * 5.checkout info object
	 */
	
	@Autowired OrderRepository repo;
	
	//method that create new order
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems,
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		
		//creates new order object
		Order newOrder = new Order();
		newOrder.setOrderTime(new Date());
		newOrder.setStatus(OrderStatus.NEW);
		newOrder.setCustomer(customer);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setTax(0.0f);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setPaymentMethod(paymentMethod);
		newOrder.setDeliveryDays(checkoutInfo.getDeliveryDays());
		newOrder.setDeliveryDate(checkoutInfo.getDeliveryDate());
		
		//check if address is null meaning if shipping address is the same as the customer address
		if (address == null) {
			newOrder.copyAddressFromCustomer();
		} else {
			newOrder.copyShippingAddress(address);
		}
		
		
		
		//get collection of order details from the main order
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		
		//set order details of the product being ordered by
		//reading product information in cart items
		//iterate through products in cart
		for (CartItem cartItem : cartItems) {
			//get products in cart
			Product product = cartItem.getProduct();
			
			//creates new order details object
			OrderDetail orderDetail = new OrderDetail();
			
			//set references to the master or main order
			orderDetail.setOrder(newOrder);
			
			//set values for the fields in the order detail object
			orderDetail.setOrder(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(cartItem.getQuantity());//thus quantity of products in cart
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
			orderDetail.setSubtotal(cartItem.getSubtotal());
			orderDetail.setShippingCost(cartItem.getShippingCost());
			
			//add the order details to the collection of order details
			orderDetails.add(orderDetail);
		}
	
		return repo.save(newOrder);
	}

}
