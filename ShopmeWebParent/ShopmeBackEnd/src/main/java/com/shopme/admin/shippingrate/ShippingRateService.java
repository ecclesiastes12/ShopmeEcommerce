package com.shopme.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.User;


@Service
public class ShippingRateService {

	@Autowired
	ShippingRateRepository shipRepo;
	@Autowired CountryRepository countryRepository;

	public static final int RATES_PER_PAGE = 5;

//	public List<ShippingRate> listAll() {
//		return (List<ShippingRate>) shipRepo.findAll();
//				
//	}
	// method that list users by pages
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
	
		//method call
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	
	
	
	//method to return list of countries
		public List<Country> listAllCountries(){
			return countryRepository.findAllByOrderByNameAsc();
		}
	
	//method that creats new shipping rate
		public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
			ShippingRate rateInDB = shipRepo.findByCountryAndState(
					rateInForm.getCountry().getId(), rateInForm.getState());
			boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
			boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);
			
			if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
				throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
							+ rateInForm.getCountry().getName() + ", " + rateInForm.getState()); 					
			}
			shipRepo.save(rateInForm);
		}
	
	//method that get shipping rate by id
	public ShippingRate getRateById(Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find any shipping rate with ID " + id);
		}
		
	}
	
	//method that update codSupport status
	public void updateCODSupport(boolean enabled, Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.updateCODSupport(enabled, id);
	}
	
	//method that delete shipping rate
	public void deleteShipRate(Integer id) throws ShippingRateNotFoundException {
		Long countId = shipRepo.countById(id);
		
		if(countId == null || countId == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.deleteById(id);
	}
}
