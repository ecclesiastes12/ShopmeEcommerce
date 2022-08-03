package com.shopme;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/**
 * this class is created for the purpose of 
 * exposing the user-photos directory to display 
 * the photos stored in directory
 */
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	//configuration method for exposing images/photos in the file directories
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		//method call
		
		exposeDirectory("../category-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);
		exposeDirectory("../site-logo", registry);
	}

	//method that exposes images in the directory refactored
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		
		//get directory path
		Path path = Paths.get(pathPattern);
		
		//get absolute path
		String absolutePath = path.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("..","") + "/**";
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + absolutePath + "/");
	}
	
}
