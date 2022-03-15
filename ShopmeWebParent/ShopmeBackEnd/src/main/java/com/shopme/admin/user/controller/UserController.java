package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {

	@Autowired
	UserService service;

	@GetMapping("/users")
	public String listFirstPage() { //listFirstPage parameters removed because listByPage method 
									//is not used to return the page
	
		//return listByPage(helper,1,model, "firstName","asc",null);
		
		//return string for sort field and direction instead of calling listByPage method 
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}
	
	/**
	 *
	 *	CHECK UserController1 to see the previous code, comments and the modification
	 *	made to listByPage method.
	 */
	
	
//	//handler method for page number
//	//@PagingAndSortingParam bind to PagingAndSortingHelper object
//	//specify the values for listName and moduleURL parameters declared in PagingAndSortingParam annotation
//	@GetMapping("/users/page/{pageNum}")    
//	public String listByPage(
//			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
//			@PathVariable(name ="pageNum") int pageNum ,Model model,
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword) {
//		
//		System.out.println("Sort Field: " + sortField);
//		System.out.println("Sort Oder: " + sortDir);
//		
//		Page<User> page = service.listByPage(pageNum,sortField,sortDir,keyword);
//		
//		//code moved to PagingAndSortingHelper class
//		//get the content to page
//		//List<User> listUsers = page.getContent();
//		
//		/*
//		 * System.out.println("pageNum =" + pageNum);
//		 * System.out.println("Total elements = " + page.getTotalElements());
//		 * System.out.println("Total Pages = " + page.getTotalPages());
//		 */
//		
//		//code moved to PagingAndSortingHelper class
//		
////		//count pages
////		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
////		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
////		
////		//gets the last page number
////		if(endCount > page.getTotalElements()) {
////			endCount = page.getTotalElements();
////		}
//		
//		// code moved to PagingAndSortingArgumentResolver class
//		//sorting
//		//String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//		
//		
//		//code moved to PagingAndSortingHelper class
////		model.addAttribute("currentPage", pageNum);
////		model.addAttribute("totalPages", page.getTotalPages());
////		model.addAttribute("startCount", startCount);
////		model.addAttribute("endCount", endCount);
////		model.addAttribute("totalItems", page.getTotalElements());
////		model.addAttribute("listUsers", listUsers);
//		
//		
//		//code moved to public Object resolveArgument(... 
//		//method in PagingAndSortingArgumentResolver class
//		
////		model.addAttribute("sortField", sortField);
////		model.addAttribute("sortDir", sortDir);
////		model.addAttribute("reverseSortDir", reverseSortDir);
////		
////		//display the keyword
////		model.addAttribute("keyword", keyword); 
//		
//		//code moved to PagingAndSortingArgumentResolver class
//		//for url, this attribute will replace some of the moduleURL in the fragments.html
//		//model.addAttribute("moduleURL", "/users"); 
//		
//		return "users/users";
//	}
//	
	
	//handler method for page number
	//@PagingAndSortingParam bind to PagingAndSortingHelper object
	//specify the values for listName and moduleURL parameters declared in PagingAndSortingParam annotation
//	@GetMapping("/users/page/{pageNum}")    
//	public String listByPage(
//			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
//			@PathVariable(name ="pageNum") int pageNum ,Model model,
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword) {
//		
//		Page<User> page = service.listByPage(pageNum,sortField,sortDir,keyword);
//		
//		//method call
//		helper.updateModelAttributes(pageNum, page);
//		
//		
//		return "users/users";
//	}
	
	//sortField,sortDir and keyword removed because those fields are now accessed
	//PagingAndSortingHelper
	@GetMapping("/users/page/{pageNum}")    
	public String listByPage(
			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name ="pageNum") int pageNum) {
		
		//Page<User> page = service.listByPage(pageNum,sortField,sortDir,keyword);
		
		//page object removed because listByPage method parameter in UserService class
		//is modified. sortField,sortDir,keyword is replaced with PagingAndSortingHelper  
		service.listByPage(pageNum,helper);
		
		
		//method call
		//this method can be called right in UserService class because listByPage
		//method in UserService class parameters is modified. Therefore method is
		//now moved to UserService class
		//helper.updateModelAttributes(pageNum, page);
		
		
		return "users/users";
	}
	
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();

		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");

		return "users/user_form";
	}

	//the parameter value of RequestParam used here is the name attribute
	//used in the input tag in the user_form.html
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes, Model model,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		//check if the form has an upload file or not. thus if multipart is not empty
		//then the form has an upload file
		if(!multipartFile.isEmpty()) {
			//gets the file name
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			//sets the file name
			user.setPhotos(fileName);
			
			//saves the data with the name of the image file
			User savedUser = service.save(user);
			
			// directory name with saved user id
			String uploadDir = "user-photos/" + savedUser.getId(); 
			
			//method call. clean the directory before saving the file
			FileUploadUtil.cleanDir(uploadDir);
			
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}else {

			//set photos to null if user photo is empty
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			
			service.save(user);

		}
		
		
		// success message
		redirectAttributes.addFlashAttribute("message", "The user has been saved succesfully.");

		// redirects to users page
		//return "redirect:/users"; 

		return getRedirectURLtoAffectedUser(user);
	}

	// method to show the edited user on the first page
	private String getRedirectURLtoAffectedUser(User user) {
		String firtsPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firtsPartOfEmail;
	}

	// NB @PathVariable is used here because the id from the url
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		// retrieve the user object by id
		// method call from the service class
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user", user);
			// both works
			// model.addAttribute("user",service.get(id));

			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "users/user_form";

		} catch (UserNotFoundException ex) {
			// redirectAttributes is for displaying the error message or custome exception
			// created in the UserService class thus UserNotFoundException in the get method
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		// retrieve the user object by id
		// method call from the service class
		try {
			service.delete(id);

			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted succesfully");

		} catch (UserNotFoundException ex) {
			// redirectAttributes is for displaying the error message or custome exception
			// created in the UserService class thus UserNotFoundException in the get method
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/users";
	}

	/**
	 * mapping of the id and stutus with the path variables
	 * 
	 * {id} must match with @PathVariable("id")
	 * {status} must match with @PathVariable("status")
	 */
	
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		
		service.updateUserEnabledStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disabled";
			
			String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/users";
	}

	//for csv
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = service.listAll();
		
		UserCsvExporter exporter = new UserCsvExporter();
		
		exporter.export(listUsers, response);
		
	}
	
	//for excel
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		
		exporter.export(listUsers, response);
		
	}
	
	//for pdf
		@GetMapping("/users/export/pdf")
		public void exportToPdf(HttpServletResponse response) throws IOException {
			
			List<User> listUsers = service.listAll();
			
			UserPdfExporter exporter = new UserPdfExporter();
			
			exporter.export(listUsers, response);
			
		}
	
}
