package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired 
	CategoryRepository repo;
	
	public List<Category> listNoChildrenCategories(){
		
		//create an arraylist object  of type category
		List<Category> listNoChildrenCategories = new ArrayList<>();
		
		//grabs list of enabled categories
		List<Category> listEnabledCategories = repo.findAllEnabled();
		
		//iterate through each list of enabled category
		listEnabledCategories.forEach(category -> {
			//set children categories
			Set<Category> children = category.getChildren();
			
			//checks if child category is null or size is zero
			if(children == null || children.size() == 0) {
				
				//add categories with no children
				listNoChildrenCategories.add(category);
			}
		});
		
		return listNoChildrenCategories;
	}
	
	//method to find enabled alias
	public Category getCatgory(String alias) throws CategoryNotFoundException {
		Category category = repo.findByAliasEnabled(alias);
		
		//checks if category object is null
		if(category == null) {
			throw new CategoryNotFoundException("Could not find any category with alias " + alias);
		}
		return category;
	}
	
	//this method is created purposely for breadcrumb
	//method to get list of parents for a specific category 
	public List<Category> getCategoryParents(Category child){
		
		//create object list of parent category in a form of arraylist
		List<Category> listParents = new ArrayList<>();
		
		//get the parent of a given category
		Category parent = child.getParent();
		
		//loops through all the parents of the category
		//breaks the loop if parent is null
		while (parent != null) {
			//adds the parent object to listPatents. thus all the parents of a specific category
			listParents.add(0, parent);
			
			parent = parent.getParent();
		}
		
		/* adds the child category to the end of listParent
		 * eg in breadcrumb 
		 * Electronics/ Cell Phones & Accessories/ Carrier Cell Phones
		 * where Carrier Cell Phones is the child category
		 */
		 
		listParents.add(child);
		
		return listParents;
	}
}
