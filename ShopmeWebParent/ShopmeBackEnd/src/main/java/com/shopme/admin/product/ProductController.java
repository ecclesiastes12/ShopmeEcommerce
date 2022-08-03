package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;



@Controller
@Transactional
public class ProductController {

	/*
	 * private static final Logger LOGGER=
	 * LoggerFactory.getLogger(ProductController.class);
	 */
	
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;

	
	
	/*
	 * @GetMapping("/products") public String listAll(Model model) {
	 * 
	 * List<Product> listProducts = productService.listAll();
	 * 
	 * model.addAttribute("listProducts", listProducts);
	 * 
	 * return "products/products"; }
	 */
	
	@GetMapping("/products")
	public String listByFirstPage(Model model) {
		
		//return listByPage(1, model, "name", "asc", null,0);	
		
		return "redirect:/products/page/1?sortField=name&sortDir=asc";
	}
	
//	@GetMapping("/products/page/{pageNum}")
//	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model,
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword,
//			@Param("categoryId") Integer categoryId //categoryId refers to the name="categoryId" in product.html
//			){
//		
//		
//		//updated with categoryId for dropdown search
//		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir,keyword,categoryId);
//		
//		List<Product> listProducts = page.getContent();
//		
//		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//		
//		// page counter
//		long startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
//		long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
//		
//		//gets the last page number
//		if(endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		
//		//reverse sorting
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//				
//		//set category id if it is not null
//		if(categoryId != null) model.addAttribute("categoryId", categoryId);
//		
//		model.addAttribute("listProducts", listProducts);
//		model.addAttribute("listCategories", listCategories);
//		
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("totalItems", page.getTotalElements());
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword", keyword); //display the keyword 
//		
//		//for url, this attribute will replace some of the moduleURL in the fragments.html
//		model.addAttribute("moduleURL", "/products");
//		
//		
//		return "products/products";
//	}

	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") int pageNum, Model model,
			@Param("categoryId") Integer categoryId //categoryId refers to the name="categoryId" in product.html
			){
		
		
		//updated with categoryId for dropdown search
		//Page<Product> page = productService.listByPage(pageNum, sortField, sortDir,keyword,categoryId);
		
		productService.listByPage(pageNum, helper,categoryId);
		
		//list categories
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
		//set category id if it is not null
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		
		
		return "products/products";
	}

	
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		List<Brand> listBrands = brandService.listAll();
		
		
		Product product = new Product();
		
		//sets enable and instock to true for default value
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		
		model.addAttribute("numberOfExistingExtraImages", 0);
		
		model.addAttribute("pageTitle", "Create New Product");
		
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, 
			RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultiparts,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser
			) throws IOException {
		
		//check if logged in user role is not Admin and Editor
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
		
			//check the role of the logged in user
			if(loggedUser.hasRole("Salesperson")) {
				//save only the price information of the product if the logged in user has the role salesperson
				productService.saveProductPrice(product);
				
				ra.addFlashAttribute("message", "The product has been saved successfully.");
				
				return "redirect:/products";
			}
		}
		/*
		 * setMainImageName(mainImageMultiparts, product); //method call
		 * setExistingExtraImageNames(imageIDs, imageNames, product);
		 * setNewExtraImageNames(extraImageMultiparts, product); //method call
		 * setProductDetails(detailIDs, detailNames,detailValues,product);
		 */
		
		ProductSaveHelper.setMainImageName(mainImageMultiparts, product); //method call
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product); //method call
		ProductSaveHelper.setProductDetails(detailIDs, detailNames,detailValues,product);
		
		Product savedProduct = productService.save(product);
		
		/*
		 * //method call that save the uploaded images
		 * saveUploadedImages(mainImageMultiparts,extraImageMultiparts,savedProduct);
		 * 
		 * //delete extra images that were removed on form
		 * deleteExtraImagesWereRemovedOnForm(product);
		 */
		
		//method call that save the uploaded images
		ProductSaveHelper.saveUploadedImages(mainImageMultiparts,extraImageMultiparts,savedProduct);
		
		//delete extra images that were removed on form
		ProductSaveHelper.deleteExtraImagesWereRemovedOnForm(product);
		
		ra.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}
	
																																									//function to remove extra images removed on form. That is when user change 
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable(name = "id") Integer id, 
		   @PathVariable(name = "status") boolean enabled,
		   RedirectAttributes redirectAttributes) {
		
		String status = "";
		productService.updateProductEnabledStatus(id, enabled);
		
		if(enabled) { status += "enabled"; }else { status += "disabled"; }
		
		redirectAttributes.addFlashAttribute("message", "The product with ID " + id + " is " + status + " successfully");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id,RedirectAttributes ra) {
	
		try {
			productService.deleteProduct(id);
			/*
			 * String brandDir = "../brand-logos/" + id; FileUploadUtil.removeDir(brandDir);
			 */
			
			String productExtraImagesDir = "../product-images/" + id + "extras";
			String productImagesDir = "../product-images/" + id;
			
			
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);
			
			ra.addFlashAttribute("message", "The product with ID: (" + id + ") has been deleted succesffully");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
	}
	
	
//	@GetMapping("/products/edit/{id}")
//	public String editProduct(@PathVariable("id") Integer id,
//			Model model,
//			RedirectAttributes ra) {
//		try {
//			Product product = productService.get(id);
//			List<Brand> listBrands = brandService.listAll();
//			Integer numberOfExistingExtraImages = product.getImages().size();
//			
//			model.addAttribute("product", product);
//			model.addAttribute("listBrands", listBrands);
//			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
//			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
//			
//			return "products/product_form";
//			
//		} catch (ProductNotFoundException ex) {
//			ra.addFlashAttribute("message", ex.getMessage());
//			
//			return "redirect:/products";
//		}
//	}
	
	//handler method modified 
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id,
			Model model,
			RedirectAttributes ra,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			//variable declaration for read only fields
			boolean isReadOnlyForSalesperson = false;
			//check if logged in user role is not Admin and Editor
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			
				//check the role of the logged in user
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesperson = true;
				}
			}
			//isReadOnlyForSalesperson being added to the model will render some fields to be read only
			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			
			return "products/product_form";
			
		} catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id,
			Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);
		
			return "products/product_detail_modal";
			
		} catch (ProductNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/products";
		}
	}
	
}
