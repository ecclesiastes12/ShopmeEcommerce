//package com.shopme.admin.paging;
//
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//import com.shopme.admin.user.UserService;
//import com.shopme.common.entity.Brand;
//import com.shopme.common.entity.User;
//
//public class PagingAndSortingHelper1 {
//
//	//variable declaration
//	ModelAndViewContainer model; //this is related to ModelAndViewContainer in PagingAndSortingArgumentResolver class
//	String moduleURL;
//	String listName;
//	
//	String sortField;
//	String sortDir;
//	String keyword;
//	
//	//ModelAndViewContainer is used here so that it can be used to update model attributes in updateModelAttributes
//	public PagingAndSortingHelper1(ModelAndViewContainer model, String moduleURL, String listName,
//			String sortField, String sortDir, String keyword) {
//		this.model = model;
//		this.moduleURL = moduleURL;
//		this.listName = listName;
//		this.sortField = sortField;
//		this.sortDir = sortDir;
//		this.keyword = keyword;
//	}
//	
//	//NB ? in Page<?> indicate that ? can be of any type
//	public void updateModelAttributes(int pageNum, Page<?> page) {
//		
//		//get the content to page
//		//List<User> listUsers = page.getContent();
//		
//		// variable name listUsers change to listItems
//		List<?> listItems = page.getContent();
//		int pageSize = page.getSize();
//		
//		//count pages
//		//long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
//		//long endCount = startCount + UserService.USERS_PER_PAGE - 1;
//		
//		//UserService.USERS_PER_PAGE replace with pageSize
//		long startCount = (pageNum - 1) * pageSize + 1;
//		long endCount = startCount + pageSize - 1;
//		
//		//gets the last page number
//		if(endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//		
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//		
//		//model.addAttribute("listUsers", listUsers);
//		
//		//model.addAttribute("listUsers", listItems);
//		
//		//attribute name listUsers change to listName
//		model.addAttribute(listName, listItems);
//		
//		//for url, this attribute will replace some of the moduleURL in the fragments.html
//		//model.addAttribute("moduleURL", "/users");
//		
//		//the value of the moduleURL which is "/users" change to moduleURL
//		model.addAttribute("moduleURL", moduleURL);
//	}
//	
//	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo) {
//		Sort sort = Sort.by(sortField);
//		
//		//sortField now accessed by helper.getSortField
//		//Sort sort = Sort.by(helper.getSortField());
//		
//		
//		//sort in ascending or descending order
//		if(sortDir.equals("asc")) {
//			sort = sort.ascending();
//		}else {
//			sort = sort.descending();
//		}
//		
//		//Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
//		
//		//BRANDS_PER_PAGE replaced with pageSize
//		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
//		
//		//page object declared with a null value because listByPage method return type is change to void
//		Page<?> page = null; //? in Page<?> means ? can be of any type eg Brand 
//		
//		//checks if keyword is not empty or not null
//		if (keyword!= null) {
//			page = repo.findAll(keyword, pageable); //returns the search results if keyword is not null
//		}else {
//		
//			page = repo.findAll(pageable);
//		}
//		
//		updateModelAttributes(pageNum, page);
//	}
//	
//	//for ProductService class
//	//method that create page object based on page size and page number
//	public  Pageable createPageable(int pageSize, int pageNum) {
//		Sort sort = Sort.by(sortField);
//		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//		
//		return PageRequest.of( pageNum - 1, pageSize, sort);
//	}
//
//	//getters only sortField, sortDir and keyword
//	public String getSortField() {
//		return sortField;
//	}
//
//	public String getSortDir() {
//		return sortDir;
//	}
//
//	public String getKeyword() {
//		return keyword;
//	}
//	
//	
//	
//	
//}
