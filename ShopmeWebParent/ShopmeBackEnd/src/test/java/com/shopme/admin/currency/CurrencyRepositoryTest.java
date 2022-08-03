package com.shopme.admin.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	CurrencyRepository repo;	
	
	@Test
	public void testCreateCurrency() {
		
		//Currency newCurrency = new Currency("United States Dollar", "$", "USD");
		
		Currency britishPound = new Currency("British Pound", "£", "GBP");
		Currency japaneseYen = new Currency("Japanese Yen", "¥", "JPY");
		Currency euro = new Currency("Euro", "€", "EUR");
		Currency russianRuble = new Currency("Russian Ruble", "₽", "RUB");
		Currency southKoreanWon = new Currency("South Korean Won", "₩", "KRW");
		Currency chineseYuan = new Currency("Chinese Yuan", "¥ ", "CNY");
		Currency brazilianReal = new Currency("Brazilian Real", "R$", "BRL");
		Currency australianDollar = new Currency("Austrailian Dollar", "$", "AUD");
		Currency canadianDollar = new Currency("Canadian Dollar", "$", "CAD");
		Currency vietnameseDong = new Currency("Vietnamese dong", "₫ ", "VND");
		Currency indianRupee = new Currency("Indian Rupee", "₹", "INR");
		
		repo.saveAll(List.of(britishPound, japaneseYen, euro, russianRuble, southKoreanWon, chineseYuan,
				brazilianReal, australianDollar, canadianDollar, vietnameseDong, indianRupee));
	}
	
	
	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();
		
		currencies.forEach(System.out::println);
		
		assertThat(currencies.size()).isGreaterThan(0);
	}
}
