package com.shopme.admin.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.customer.CustomerService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class OrderController {

	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";;
	
	@Autowired OrderService orderService;
	@Autowired CustomerService customerService;
	@Autowired SettingService settingService;
	
	@GetMapping("/orders")
	public String listFirstPage() { //listFirstPage parameters removed because listByPage method 
									//is not used to return the page
	
		//return listByPage(helper,1,model, "firstName","asc",null);
		
		//return string for sort field and direction instead of calling listByPage method 
		//return "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
		return defaultRedirectURL;
	}
	
	
//	//handler method that list orders by page
//	@GetMapping("/orders/page/{pageNum}")
//	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
//			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir,
//			 String keyword, HttpServletRequest request) {
//
//		System.out.println("Sort Field: " + sortField);
//		System.out.println("Sort Oder: " + sortDir);
//
//		Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
//
//		// get the content to page
//		List<Order> listOrders = page.getContent();
//
//		/*
//		 * System.out.println("pageNum =" + pageNum);
//		 * System.out.println("Total elements = " + page.getTotalElements());
//		 * System.out.println("Total Pages = " + page.getTotalPages());
//		 */
//
//		// count pages
//		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
//		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
//
//		// gets the last page number
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}
//
//		// sorting
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", page.getTotalElements());
//
//		model.addAttribute("listOrders", listOrders);
//
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword", keyword); // display the keyword
//
//		// for url, this attribute will replace some of the moduleURL in the
//		// fragments.html
//		model.addAttribute("moduleURL", "/orders");
//		
//		loadCurrencySetting(request);
//
//		return "orders/orders";
//	}
	
	//handler method that list orders by page modified with custom annotation @PagingAndSortingParam
		@GetMapping("/orders/page/{pageNum}")
		public String listByPage(
				@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper, 
				@PathVariable(name = "pageNum") Integer pageNum, HttpServletRequest request) {

			orderService.listByPage(pageNum, helper);
			
			loadCurrencySetting(request);

			return "orders/orders";
		}
	
	//handler method that load currency settings
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}	
	
	//handler method that show order details of a customer
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable(name = "id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) {
		
		try {
			//get order by id
			Order order = orderService.get(id);
			
			//load currency settings
			loadCurrencySetting(request);
			
			model.addAttribute("order", order);
		
			return "orders/order_detail_modal";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			//return "redirect:/orders";
			return defaultRedirectURL;
		}
		
	}
	
	//handler method that delete order
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable(name="id") Integer id,Model model, RedirectAttributes ra) {
		try {
			orderService.delete(id);
			ra.addFlashAttribute("message", "The order ID " + id + " has been deleted");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		//return "redirect:/orders";
		return defaultRedirectURL;
	}
}
