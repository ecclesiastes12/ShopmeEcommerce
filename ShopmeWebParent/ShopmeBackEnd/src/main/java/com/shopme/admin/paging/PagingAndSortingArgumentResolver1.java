//package com.shopme.admin.paging;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//public class PagingAndSortingArgumentResolver1 implements HandlerMethodArgumentResolver {
//
//	@Override
//	public boolean supportsParameter(MethodParameter parameter) {
//		// This PagingAndSortingArgumentResolver will support parameters that is annotated
//		//using PagingAndSortingParam annotation
//		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
//	}
//
//	@Override
//	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
//			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
//		
//		//get the parameter annotation of class type PagingAndSortingParam
//		// the purpose of this annotation is read the value of moduleURL and listName parameters
//		//declared  in PagingAndSortingParam annotation
//	 	PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
//			 	
//		//read the values of sortField,sortDir and keyword using the request object
//		String sortField = request.getParameter("sortField");
//		String sortDir = request.getParameter("sortDir");
//		String keyword = request.getParameter("keyword");
//		
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		
//		//display the keyword
//		model.addAttribute("keyword", keyword); 
//		
//		
//		// return object of type PagingAndSortingHelper 
//		//return new PagingAndSortingHelper(model, annotation.moduleURL(), annotation.listName());
//	 	
//	 	return new PagingAndSortingHelper(model, annotation.moduleURL(), annotation.listName(),
//	 			sortField,sortDir,keyword);
//	}
//
//}
