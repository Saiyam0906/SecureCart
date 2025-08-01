package com.example.SecureCart.Cloudinary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaudinaryResult {
	private String imageUrl;
    private String publicId;
    private String format;
    private String resourceType;
}
