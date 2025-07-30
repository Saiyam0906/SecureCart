package com.example.Ecommerce.Controller;


import java.sql.SQLException;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.Ecommerce.Request.ImageDto;
import com.example.Ecommerce.Response.ApiResponse;
import com.example.Ecommerce.Service.ImageService;
import com.example.Ecommerce.model.Image;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${myapp.base.url}/images")
public class ImageController {
	
	private final ImageService imageservice;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> file,@RequestParam Long productId){
	     try {
		List<ImageDto> imageDtos=imageservice.saveImage(file, productId);
		return ResponseEntity.ok(new ApiResponse("Uplpoad SuccessFull", imageDtos));
	     }catch(Exception e) {
	    	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed", e.getMessage()));
	     }
	}
	
	//Add blob data to ImageDto (not recommended for memory reasons)
	// Create a separate method in service to get blob data
	
	//        you create a separate method in service:
			// byte[] imageData = imageservice.getImageBlobById(imageId);
	
	@GetMapping("/downloads/{imageId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException{
		try {
			
			Image image=imageservice.getImageMetadataById(imageId);
			
			byte[] data = imageservice.getImageBlobById(imageId);
			
			    Resource resource = new ByteArrayResource(data);
			

		        return ResponseEntity.ok()
		        		.contentType(MediaType.parseMediaType(image.getFileType()))
		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
		                .body(resource);
		}catch (Exception e) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body(null);
		}
	}
	
	@PutMapping("/update/{imageId}")
	public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId,@RequestParam MultipartFile file){
		try {
		ImageDto image=imageservice.getImageById(imageId);
		if(image!=null) {
			imageservice.updateImage(file, imageId);
			ImageDto updatedImageDto = imageservice.getImageById(imageId);
			return ResponseEntity.ok(new ApiResponse("Image updated Successfully", image));
		}else {
			  return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("Image not found", null));
		}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed to update", e.getMessage()));
		}
		
	}
	
	@PutMapping("/delete/{imageId}")
	public ResponseEntity<ApiResponse> DeleteImage(@PathVariable Long imageId){
		try {
		ImageDto image=imageservice.getImageById(imageId);
		if(image!=null) {
			imageservice.deleteImageById(imageId);
			return ResponseEntity.ok(new ApiResponse("Image updated Successfully", image));
		}else {
			  return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse("Image not found", null));
		}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed to Delete", e.getMessage()));
		}
		
	}
	
	
	

}
