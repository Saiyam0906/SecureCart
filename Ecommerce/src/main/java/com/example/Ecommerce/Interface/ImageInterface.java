package com.example.Ecommerce.Interface;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.Ecommerce.Request.ImageDto;
import com.example.Ecommerce.model.Image;

public interface ImageInterface {
	
       ImageDto getImageById(Long id);
       void deleteImageById(Long id);
       List<ImageDto> saveImage(List<MultipartFile> files,Long productId);
       void updateImage(MultipartFile file,Long imageId);
	
       //MultipartFile is used when you want to upload a file from a 
       //user's computer to your web application.
	

}
