package com.shopme.checkout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {

	float productCost;
	float productTotal;
	float shippingCostTotal;
	float paymentTotal;
	int deliveryDays;
	boolean codSupported;
	public float getProductCost() {
		return productCost;
	}
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	public float getProductTotal() {
		return productTotal;
	}
	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}
	public float getShippingCostTotal() {
		return shippingCostTotal;
	}
	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}
	public float getPaymentTotal() {
		return paymentTotal;
	}
	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	public int getDeliveryDays() {
		return deliveryDays;
	}
	public void setDeliveryDays(int deliveryDays) {
		this.deliveryDays = deliveryDays;
	}
	
	
	
	//method that calculate the expected deliver date based on
	//deliver days
	public Date getDeliveryDate() {
		
		//creates calendar object
		Calendar calendar = Calendar.getInstance();
		
		//add the number of deliver days to the calendar
		calendar.add(Calendar.DATE, deliveryDays);
		
		return calendar.getTime();
	}
	

	public boolean isCodSupported() {
		return codSupported;
	}
	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}
	
	//method to format payment for paypal
	public String getPaymentTotal4PayPal() {
		//format the currency
		DecimalFormat formatter = new DecimalFormat("###,###.##");
		return formatter.format(paymentTotal);
	}
	
	
}
