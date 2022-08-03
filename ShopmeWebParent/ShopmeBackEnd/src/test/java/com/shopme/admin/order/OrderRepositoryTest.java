package com.shopme.admin.order;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

	@Autowired OrderRepository repo;
	@Autowired TestEntityManager entityManager;
	
	//NB the address information of the recipient is saved in the main order because
	// the address information of the recipient can be changed over time. so address
	//of the recipient at the time of purchase has to be saved
	@Test
	public void testCreateNewOrderWithSingleProduct() {
		//get the customer object from the database using entity manager
		Customer customer = entityManager.find(Customer.class, 15);
		Product product = entityManager.find(Product.class, 8);
		
		//creates order object
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer); //customer who placed the order
//		mainOrder.setFirstName(customer.getFirstName());
//		mainOrder.setLastName(customer.getLastName());
//		mainOrder.setPhoneNumber(customer.getPhoneNumber());
//		mainOrder.setAddressLine1(customer.getAddressLine1());
//		mainOrder.setAddressLine2(customer.getAddressLine2());
//		mainOrder.setCity(customer.getCity());
//		mainOrder.setCountry(customer.getCountry().getName());
//		mainOrder.setPostalCode(customer.getPostalCode());
//		mainOrder.setState(customer.getState());
		
		mainOrder.copyAddressFromCustomer();
		
		//set information about the product prices
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());//for one product
		mainOrder.setTax(0);
		mainOrder.setSubtotal(product.getPrice());
		//NB total is product price + shipping cost
		mainOrder.setTotal(product.getPrice() + 10);
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliveryDate(new Date());
		mainOrder.setDeliveryDays(1);
		
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice()); //subtotal is product price * quantity
		orderDetail.setUnitPrice(product.getPrice());
		
		//add the orderDetail to the main order
		mainOrder.getOrderDetails().add(orderDetail);
		
	    Order savedOrder = repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateNewOrderWithMultipleProduct() {
		//get the customer object from the database using entity manager
		Customer customer = entityManager.find(Customer.class, 5);
		Product product1 = entityManager.find(Product.class, 8);
		Product product2 = entityManager.find(Product.class, 15);
		
		//creates order object
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer); //customer who placed the order
		mainOrder.copyAddressFromCustomer();
		
		//order detail for product 1
		OrderDetail orderDetail1 = new OrderDetail();
		orderDetail1.setProduct(product1);
		orderDetail1.setOrder(mainOrder);
		orderDetail1.setProductCost(product1.getCost());
		orderDetail1.setShippingCost(10);
		orderDetail1.setQuantity(1);
		orderDetail1.setSubtotal(product1.getPrice()); //subtotal is product1 price * quantity
		orderDetail1.setUnitPrice(product1.getPrice());
		
		//order detail for product 2
		OrderDetail orderDetail2 = new OrderDetail();
		orderDetail2.setProduct(product2);
		orderDetail2.setOrder(mainOrder);
		orderDetail2.setProductCost(product2.getCost());
		orderDetail2.setShippingCost(20);
		orderDetail2.setQuantity(2);
		orderDetail2.setSubtotal(product2.getPrice() * 2); //subtotal is product2 price * quantity
		orderDetail2.setUnitPrice(product2.getPrice());
		
		//add the orderDetail to the main order
		mainOrder.getOrderDetails().add(orderDetail1);
		mainOrder.getOrderDetails().add(orderDetail2);
		
		//set information about the product prices
		mainOrder.setShippingCost(30);
		mainOrder.setProductCost(product1.getCost() + product2.getCost());//for one product
		mainOrder.setTax(0);
		float subtotal = product1.getPrice() + product2.getPrice() * 2;
		mainOrder.setSubtotal(subtotal);
		//NB total is product price + shipping cost
		mainOrder.setTotal(subtotal + 30);
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.SHIPPING);
		mainOrder.setDeliveryDate(new Date());
		mainOrder.setDeliveryDays(3);
		
		Order savedOrder = repo.save(mainOrder);
		assertThat(savedOrder.getId()).isGreaterThan(0);
	}
	
	//test method that list orders in the table
	@Test
	public void testListOrders() {
		Iterable<Order> orders = repo.findAll();
		
		assertThat(orders).hasSizeGreaterThan(0);
		
		orders.forEach(System.out::println);
	}
	
	//test method that update order
	@Test
	public void testUpdateOrder() {
		Integer orderId = 2;
		Order order = repo.findById(orderId).get();
		
		order.setStatus(OrderStatus.SHIPPING);
		order.setPaymentMethod(PaymentMethod.COD);
		order.setOrderTime(new Date());
		order.setDeliveryDays(2);
		
		Order updatedOrder = repo.save(order);		
		
		assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
	}
	
	//test method that get order by order id
	@Test
	public void testGetOrder() {
		Integer orderId = 3;
		Order order = repo.findById(orderId).get();
				
		assertThat(order).isNotNull();
		System.out.println(order);
		
	}
	
	//test method that delete order
	@Test
	public void testDeleteOrder() {
		Integer orderId = 3;
		repo.deleteById(orderId);
		
		Optional<Order> result = repo.findById(orderId);
		assertThat(result).isNotPresent();
	}
}
