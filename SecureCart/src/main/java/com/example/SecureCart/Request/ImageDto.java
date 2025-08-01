package com.example.SecureCart.Request;

import lombok.Data;

@Data
public class ImageDto {
	
	private Long imageId;
	private String imageName;
	private String downloadUrl;
	private String fileType;

}
