package com.example.Ecommerce.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Ecommerce.Exception.ImageNotFoundException;
import com.example.Ecommerce.Repository.ImageRepository;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Request.ImageDto;
import com.example.Ecommerce.model.Image;
import com.example.Ecommerce.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageInterface{

	
	private final ImageRepository imagerepository;
	
	private final ProductRepository productrepository;
	
	
	@Override
	public Image getImageById(Long id) {
		return imagerepository.findById(id)
				.orElseThrow(()-> new ImageNotFoundException("Image not found"));

	}

	@Override
	public void deleteImageById(Long id) {
		imagerepository.findById(id).ifPresentOrElse(imagerepository::delete, ()->{ throw new ImageNotFoundException("Image not found");});
		
	}

	@Override
	public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
		Product product=productrepository.getById(productId);
		List<ImageDto> savedImages = new ArrayList<>();
		for(MultipartFile file:files) {
			
			try {
				Image image=new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);
				
				Image savedImage = imagerepository.save(image);
				
			String downlaod = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(savedImage.getId().toString())
                    .path("/download")
                    .toUriString();
			
			savedImage.setDownloadUrl(downlaod);
		   imagerepository.save(savedImage);
		  
		   ImageDto dto = new ImageDto();
		   dto.setImageId(savedImage.getId());
		   dto.setImageName(savedImage.getFileName());
		   dto.setDownloadUrl(savedImage.getDownloadUrl());
				   

	            savedImages.add(dto);
		  
						
			}catch (Exception e) {
				throw new RuntimeException("Failed to save image"+file.getOriginalFilename(),e);
			}
		}
		return savedImages;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image=getImageById(imageId);
		try {
		image.setFileName(file.getOriginalFilename());
		
		//here converting byte[] into blob as byte cant be saved as blob
		image.setImage(new SerialBlob(file.getBytes()));
		
		imagerepository.save(image);
		}catch(IOException | SQLException e) {
			throw new RuntimeException("Failed to update image",e);
		}
		
	}

	
	

}
