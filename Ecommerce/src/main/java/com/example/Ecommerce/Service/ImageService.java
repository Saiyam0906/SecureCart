package com.example.Ecommerce.Service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Ecommerce.Exception.ImageNotFoundException;
import com.example.Ecommerce.Mapper.ImageMapper;
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
	
	private final ImageMapper imagemapper;
	
	
	@Override
	public ImageDto getImageById(Long id) {
		Image image= imagerepository.findById(id)
				.orElseThrow(()-> new ImageNotFoundException("Image not found"));
		
		return imagemapper.toDto(image);

	}

	@Override
	public void deleteImageById(Long id) {
		imagerepository.findById(id).ifPresentOrElse(imagerepository::delete, ()->{ throw new ImageNotFoundException("Image not found");});
		
	}

	@Override
	public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
	    Product product = productrepository.findById(productId)
	            .orElseThrow(() -> new RuntimeException("Product not found")); // safer than getById

	    List<ImageDto> savedImages = new ArrayList<>();

	    for (MultipartFile file : files) {
	        try {
	            Image image = new Image();
	            image.setFileName(file.getOriginalFilename());
	            image.setFileType(file.getContentType());
	            image.setImage(new SerialBlob(file.getBytes()));
	            image.setProduct(product);

	            Image savedImage = imagerepository.save(image);

	            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
	                    .path("/images/")
	                    .path(savedImage.getId().toString())
	                    .path("/download")
	                    .toUriString();

	            savedImage.setDownloadUrl(downloadUrl);
	            imagerepository.save(savedImage); // to update downloadUrl

	            ImageDto dto = imagemapper.toDto(savedImage); 
	            savedImages.add(dto);

	        } catch (Exception e) {
	            throw new RuntimeException("Failed to save image: " + file.getOriginalFilename(), e);
	        }
	    }

	    return savedImages;
	}
	
	private Image getImageEntityById(Long id) {
        return imagerepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image not found"));
    }

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image=getImageEntityById(imageId);
		try {
		image.setFileName(file.getOriginalFilename());
		
		//here converting byte[] into blob as byte cant be saved as blob
		image.setImage(new SerialBlob(file.getBytes()));
		
		imagerepository.save(image);
		}catch(IOException | SQLException e) {
			throw new RuntimeException("Failed to update image",e);
		}
		
	}
	public byte[] getImageBlobById(Long id) {
	    Image image = imagerepository.findById(id)
	            .orElseThrow(() -> new ImageNotFoundException("Image not found"));
	    
	    try {
	        Blob blob = image.getImage();
	        return blob.getBytes(1, (int) blob.length());
	    } catch (SQLException e) {
	        throw new RuntimeException("Failed to get image data", e);
	    }
	}
	
	public Image getImageMetadataById(Long id) {
	    return imagerepository.findById(id)
	            .orElseThrow(() -> new ImageNotFoundException("Image not found"));
	}
	
	

}
