package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	/*
	 * //viewCategory by alias. //@PathVariable is used to bind the URL
	 * "/c/{category_alias}"
	 * 
	 * @GetMapping("/c/{category_alias}") public String
	 * viewCategory(@PathVariable("category_alias") String alias, Model model) {
	 * //creates category object Category category =
	 * categoryService.getCatgory(alias);
	 * 
	 * //returns the error page if category is null if(category == null) { return
	 * "error/404"; }
	 * 
	 * //gets the parents of a category object in a list List<Category>
	 * listCategoryParents = categoryService.getCategoryParents(category);
	 * 
	 * 
	 * model.addAttribute("pageTitle", category.getName());//change the page title
	 * to the name of the selected category item
	 * model.addAttribute("listCategoryParents", listCategoryParents); return
	 * "products_by_category"; }
	 */
	
	//viewCategory by alias change to viewCategoryFirstPage
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
			Model model) {
		
		return viewCategoryByPage(alias, 1, model);
	}
	
	//method that viewCategoryByPage
	//@PathVariable is used to bind the URL "/c/{category_alias}"
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_alias") String alias, 
				@PathVariable("pageNum") Integer pageNum,
				Model model) {
			
			try {
				
				//creates category object (alias)
				Category category = categoryService.getCatgory(alias);
				
				//gets the parents of a category object in a list
				//for breadcrumb
				List<Category> listCategoryParents = categoryService.getCategoryParents(category);
				
				//view product by page
				Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
				
				//add content to the module
				List<Product> listProducts = pageProducts.getContent();
				
				// page counter
				long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
				long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
				
				//gets the last page number
				if(endCount > pageProducts.getTotalElements()) {
					endCount = pageProducts.getTotalElements();
				} 
				
				
				
				model.addAttribute("currentPage", pageNum);
				model.addAttribute("totalPages", pageProducts.getTotalPages());
				model.addAttribute("totalItems", pageProducts.getTotalElements());
				model.addAttribute("startCount", startCount);
				model.addAttribute("endCount", endCount);
				
				
				model.addAttribute("pageTitle", category.getName());//change the page title to the name of the selected category item
				model.addAttribute("listCategoryParents", listCategoryParents);
				model.addAttribute("listProducts", listProducts);
				model.addAttribute("category", category); //alias of the current category 
				
				return "product/products_by_category";
				
			} catch (CategoryNotFoundException e) {
				return "error/404";
			}
		}
	
	//method to view product details
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
		
		try {
			
			Product product = productService.getProduct(alias);
			
			//for breadcrumb
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
			
			model.addAttribute("product", product);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/products_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	//search by first page
	@GetMapping("/search")
	public String searchFirstPage(@Param("keyword") String keyword, Model model) {
		
		return searchByPage(keyword, 1, model); //method call
	}
	
	
	//method that search by keyword and page number. NB check the repository to see how the search was created
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@Param("keyword") String keyword, 
			@PathVariable("pageNum") int pageNum, Model model) {
		
	    Page<Product> pageProducts = productService.search(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();
				
		
		// page counter
		long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
		
		//gets the last page number
		if(endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		} 
		
		
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("pageTitle", keyword + " - Search Result ");//change the page title to the name of the searched keyword
		
		
		
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listResult", listResult);	
		return "product/search_result";
	}
	
}
