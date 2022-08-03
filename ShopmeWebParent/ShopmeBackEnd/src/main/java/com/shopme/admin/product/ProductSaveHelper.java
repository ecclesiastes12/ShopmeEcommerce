package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger  LOGGER= LoggerFactory.getLogger(ProductSaveHelper.class);

	/*
	 * 
	 * BEFORE
	 */
	
//	// or remove an existing image
//		private void deleteExtraImagesWereRemovedOnForm(Product product) {
//			// directory path
//			String extraImageDir = "../product-images/"+ product.getId() + "/extras";
//			Path dirPath = Paths.get(extraImageDir);
//			
//			try {
//				// loops through each extra image file
//				Files.list(dirPath).forEach(file -> {
//					//gets file name
//					String filename = file.toFile().getName();
//					
//					//checks if product does not contains the file name
//					if(!product.containsImageName(filename)) {
//						try {
//							Files.delete(file);
//							LOGGER.info("Delete extra image: " + filename);
//							
//						} catch (Exception e) {
//							LOGGER.error("Could not delete extra image" + filename);
//						}
//					}
//				});
//			} catch (Exception e) {
//				LOGGER.error("Could not list directory" + dirPath);
//			}
//			
//			
//		}
//
//		//method to set id's and names of existing extra images
//		private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
//				Product product) {
//			//checks if the array of imageIDs is null or length of the array is zero
//			if(imageIDs == null || imageIDs.length == 0) return;
//			
//			Set<ProductImage> images = new HashSet<>();
//			
//			//loops throught the array of images
//			for(int count = 0; count < imageIDs.length; count++) {
//				//grabs  the id of the array of images and convert it into integer
//				Integer id = Integer.parseInt(imageIDs[count]);
//				
//				//grabs the image name from the array at index count
//				String name = imageNames[count];
//				
//				images.add(new ProductImage(id, name, product));
//			}
//			
//			product.setImages(images);
//		}
//
//		//method that set product details
//		private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
//			// check if there is no null elements in the array of product details
//			//or the length of the array is not equal to zero
//			if(detailNames == null || detailNames.length == 0) return ;
//			
//			//iterate through each detailNames in the array
//			for(int count = 0; count < detailNames.length; count++) {
//				
//				//gets the names and values in the array at index count
//				String name = detailNames[count];
//				String value = detailValues[count];
//				Integer id = Integer.parseInt(detailIDs[count]);
//				
//				//checks if id is not zero or id exist
//				if(id != 0) {
//					product.addDetail(id,name,value);
//				}else if(!name.isEmpty() && !value.isEmpty()) {//checks if name and value is not empty
//					//adds the product details to the products
//					product.addDetail(name, value);
//				}
//				
//			}
//			
//			
//		}
//
//		//method that save main images
//		private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
//				Product savedProduct) throws IOException {
//			//check if imagepath is not empty. if not empty it means the form is in the edit mode
//			if(!mainImageMultipart.isEmpty()) {
//				
//				String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
//				
//				String uploadDir = "../product-images/" + savedProduct.getId();
//				
//				FileUploadUtil.cleanDir(uploadDir);
//				
//				FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
//			}
//			
//			// save extra images
//			//loops through the extraimage if the length is more than o
//			if(extraImageMultiparts.length > 0) {
//				
//				//create directory for extra images
//				String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
//				for(MultipartFile multipartFile : extraImageMultiparts) {
//					//checks if multipartFile is empty
//					if(multipartFile.isEmpty()) {
//						// continue keyword used here causes the loop to jump to the next 
//						//iteration if multipartFile is empty
//						continue; 
//					}
//					
//					//grabs the file name
//					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//					
//					//save the upload file to the image directory
//					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//				}
//			}
//			
//		}
//
//		//method to set main image name
//		private void setMainImageName(MultipartFile mainImageMultiparts, Product product) {
//			if(!mainImageMultiparts.isEmpty()) {
//				
//				String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());
//				
//				product.setMainImage(fileName);
//			}
//		}
//		
//		//method to set extra image names
//		private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
//			//loops through the extraimage if the length is more than o
//			if(extraImageMultiparts.length > 0) {
//				for(MultipartFile multipartFile : extraImageMultiparts) {
//					//checks if multipartFile is not empty
//					if(!multipartFile.isEmpty()) {
//						//grabs the file name
//						String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//						
//						//checks if product does not contain imageName
//						if(!product.containsImageName(fileName)) {
//							// adds the file name
//							product.addExtraImage(fileName);
//						}
//					}
//				}
//			}
//		}
	
	
	/*
	 * AFTER
	 */
	
	// or remove an existing image
	static void deleteExtraImagesWereRemovedOnForm(Product product) {
			// directory path
			String extraImageDir = "../product-images/"+ product.getId() + "/extras";
			Path dirPath = Paths.get(extraImageDir);
			
			try {
				// loops through each extra image file
				Files.list(dirPath).forEach(file -> {
					//gets file name
					String filename = file.toFile().getName();
					
					//checks if product does not contains the file name
					if(!product.containsImageName(filename)) {
						try {
							Files.delete(file);
							LOGGER.info("Delete extra image: " + filename);
							
						} catch (Exception e) {
							LOGGER.error("Could not delete extra image" + filename);
						}
					}
				});
			} catch (Exception e) {
				LOGGER.error("Could not list directory" + dirPath);
			}
			
			
		}

		//method to set id's and names of existing extra images
		static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
				Product product) {
			//checks if the array of imageIDs is null or length of the array is zero
			if(imageIDs == null || imageIDs.length == 0) return;
			
			Set<ProductImage> images = new HashSet<>();
			
			//loops throught the array of images
			for(int count = 0; count < imageIDs.length; count++) {
				//grabs  the id of the array of images and convert it into integer
				Integer id = Integer.parseInt(imageIDs[count]);
				
				//grabs the image name from the array at index count
				String name = imageNames[count];
				
				images.add(new ProductImage(id, name, product));
			}
			
			product.setImages(images);
		}

		//method that set product details
		static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
			// check if there is no null elements in the array of product details
			//or the length of the array is not equal to zero
			if(detailNames == null || detailNames.length == 0) return ;
			
			//iterate through each detailNames in the array
			for(int count = 0; count < detailNames.length; count++) {
				
				//gets the names and values in the array at index count
				String name = detailNames[count];
				String value = detailValues[count];
				Integer id = Integer.parseInt(detailIDs[count]);
				
				//checks if id is not zero or id exist
				if(id != 0) {
					product.addDetail(id,name,value);
				}else if(!name.isEmpty() && !value.isEmpty()) {//checks if name and value is not empty
					//adds the product details to the products
					product.addDetail(name, value);
				}
				
			}
			
			
		}

		//method that save main images
		static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
				Product savedProduct) throws IOException {
			//check if imagepath is not empty. if not empty it means the form is in the edit mode
			if(!mainImageMultipart.isEmpty()) {
				
				String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
				
				String uploadDir = "../product-images/" + savedProduct.getId();
				
				FileUploadUtil.cleanDir(uploadDir);
				
				FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
			}
			
			// save extra images
			//loops through the extraimage if the length is more than o
			if(extraImageMultiparts.length > 0) {
				
				//create directory for extra images
				String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
				for(MultipartFile multipartFile : extraImageMultiparts) {
					//checks if multipartFile is empty
					if(multipartFile.isEmpty()) {
						// continue keyword used here causes the loop to jump to the next 
						//iteration if multipartFile is empty
						continue; 
					}
					
					//grabs the file name
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					//save the upload file to the image directory
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				}
			}
			
		}

		//method to set main image name
		static void setMainImageName(MultipartFile mainImageMultiparts, Product product) {
			if(!mainImageMultiparts.isEmpty()) {
				
				String fileName = StringUtils.cleanPath(mainImageMultiparts.getOriginalFilename());
				
				product.setMainImage(fileName);
			}
		}
		
		//method to set extra image names
		static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
			//loops through the extraimage if the length is more than o
			if(extraImageMultiparts.length > 0) {
				for(MultipartFile multipartFile : extraImageMultiparts) {
					//checks if multipartFile is not empty
					if(!multipartFile.isEmpty()) {
						//grabs the file name
						String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
						
						//checks if product does not contain imageName
						if(!product.containsImageName(fileName)) {
							// adds the file name
							product.addExtraImage(fileName);
						}
					}
				}
			}
		}
}
