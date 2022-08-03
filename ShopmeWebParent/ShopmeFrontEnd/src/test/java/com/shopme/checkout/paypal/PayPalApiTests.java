package com.shopme.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AbYtx4yu74D4DwMonp0lhLxuTVz9n93wztX3uxra2F0Rqw6yBhFf9rrZiYy7Y9e-kMKfhaXVzXn0LDMW";
	private static final String CLIENT_SECRET = "EJMQ9LkkVEwJI8yVwQJbUgGWO3EELwmoaqBp6NY4Kch4s2MK0AJ6Q9Dx76G6sux3rc4XoiYJ5KKYC0-e";
	
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "123Abc";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		//create http header
		HttpHeaders headers = new HttpHeaders();
		
		//set accept media type
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		//add hearder accept language
		headers.add("Accept-Language", "en_US");
		
		//set authorized information client id and client secret code
		headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
		
		//create new http entity generic type
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		
		//creates rest template object
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, String.class);
		
		System.out.println(response);
		
	}
	
	
}
