package com.shopme.admin;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/**
 * this class is created for the purpose of 
 * exposing the user-photos directory to display 
 * the photos stored in directory
 */
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	//configuration method for exposing images/photos in the file directories
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * //exposes photos in the user-photo directory
		 * 
		 * String dirName = "user-photos"; //directory path Path userPhotosDir =
		 * Paths.get(dirName);
		 * 
		 * //get absolute path String userPhotosPath =
		 * userPhotosDir.toFile().getAbsolutePath();
		 * 
		 * registry.addResourceHandler("/" + dirName + "/**")
		 * .addResourceLocations("file:/" + userPhotosPath + "/");
		 * 
		 * //exposes images in category-image directory String categoryImagesDirName =
		 * "../category-images"; //directory path Path categoryImagesDir =
		 * Paths.get(categoryImagesDirName);
		 * 
		 * //get absolute path String categoryImagesPath =
		 * categoryImagesDir.toFile().getAbsolutePath();
		 * 
		 * registry.addResourceHandler("/category-images/**")
		 * .addResourceLocations("file:/" + categoryImagesPath + "/");
		 * 
		 * //exposes images in brand-image directory String brandImagesDirName =
		 * "../brand-logos"; //directory path Path brandImagesDir =
		 * Paths.get(brandImagesDirName);
		 * 
		 * //get absolute path String brandImagesPath =
		 * brandImagesDir.toFile().getAbsolutePath();
		 * 
		 * registry.addResourceHandler("/brand-logos/**") .addResourceLocations("file:/"
		 * + brandImagesPath + "/");
		 */
		
		//method call
		exposeDirectory("user-photos", registry);
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

	//registers custom argument resolver
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
	
}
