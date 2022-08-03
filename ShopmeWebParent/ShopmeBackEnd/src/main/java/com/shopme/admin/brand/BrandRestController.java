package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	BrandService service;

	/*
	 * NB The use of @RequestParam is optional if and only if the name of variable in the arguement
	 * is the same as that of the parameters in the request. eg public String checkDuplicateEmail(Integer id, String email){...}
	 * 
	 *  Use @RequestParam when you want to set default value or 
	 *  when parameter is not required or
	 *  when parameter name is different from arguement name
	 *  eg 
	 *  @GetMapping("/url")
	 *  public String handlerMethod(@RequestParam(name = "name", required = false), String productName){
	 *    //..
	 *  }
	 *  
	 */
	
	@PostMapping("/brands/check_unique")
	//public String checkUnique( @RequestParam("id") Integer id,  @RequestParam("name") String name) {
	public String checkUnique(Integer id, String name) {
		return service.checkUnique(id, name);
	}

	// purpose of this restcontroller is to call the brand with
	// its associated category on the product form

	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId)
			throws BrandNotFoundRestException {

		// return list of category object
		List<CategoryDTO> listCategories = new ArrayList<>();

		try {
			// grabs brands id

			Brand brand = service.get(brandId);

			// sets categories for brands used in product
			Set<Category> categories = brand.getCategories();

			// iterate through each categories associated with the brand
			for (Category category : categories) {

				// create new CategoryDTO object that takes category id and category name
				// as parameter
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());

				// adds the dto object to the list of categories
				listCategories.add(dto);
			}

			return listCategories;

		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}

}
