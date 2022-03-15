package com.shopme.admin.paging;
/*
 * NB this is a custom annotation.
 */

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface PagingAndSortingParam {

	//Parameter declaration for model attribute name  listUsers and  moduleURL value /users
	//in PagingAndSortingArgumentResolver class
	public String moduleURL();
	
	public String listName();
}
