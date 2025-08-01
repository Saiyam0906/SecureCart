package com.example.SecureCart.Cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

	private final Cloudinary cloudinary;
	
	@SuppressWarnings("unchecked")
	 public ClaudinaryResult uploadImage(MultipartFile file, String folder, String publicId) throws IOException {
		if (file == null || file.isEmpty()) {
		    throw new IllegalArgumentException("File must not be empty");
		}
		if (!file.getContentType().startsWith("image/")) {
		    throw new IllegalArgumentException("Only image files are allowed");
		}
	        @SuppressWarnings("unchecked")
			Map<String, Object> uploadResult = cloudinary.uploader().upload(
	                file.getBytes(),
	                ObjectUtils.asMap(
	                        "folder", folder,
	                        "public_id", publicId,
	                        "overwrite", true,
	                        "resource_type", "image",
	                        "transformation", ObjectUtils.asMap(
	                        		"width", 300,
	                                "height", 300,
	                                "crop", "fill",
	                                "gravity", "face",
	                                "format", "jpg",
	                                "quality", "auto"
	                        )
	                )
	        );

	        String imageUrl = (String) uploadResult.get("secure_url");
            String  actualPublicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            String resourceType = (String) uploadResult.get("resource_type");
            
            log.info("Uploaded image: publicId={}, url={}", actualPublicId, imageUrl);

            return ClaudinaryResult.builder()
                    .imageUrl(imageUrl)
                    .publicId(actualPublicId)
                    .format(format)
                    .resourceType(resourceType)
                    .build();
	    }
	 
	 public void deleteImage(String publicId) {
	        try {
	            if (publicId != null && !publicId.trim().isEmpty()) {
	                Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	                String deleteResult = (String) result.get("result");
	                
	                if ("ok".equals(deleteResult)) {
	                    log.info("Successfully deleted image: {}", publicId);
	                } else {
	                    log.warn("Image deletion returned status '{}' for publicId: {}", deleteResult, publicId);
	                }
	            }
	        } catch (Exception e) {
	            log.error("Failed to delete image with publicId {}: {}", publicId, e.getMessage());
	          
	        }
	    }
	 
	 public String generateTransformedUrl(String publicId, int width, int height, String crop) {
	        return cloudinary.url()
	        		.transformation(new Transformation<>()
	        	            .width(width)
	        	            .height(height)
	        	            .crop(crop)
	        	            .fetchFormat("auto")
	        	            .quality("auto")
	        	        )
	        	        .generate(publicId);
	    }
	
	
	
}
