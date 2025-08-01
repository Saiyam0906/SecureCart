package com.example.SecureCart.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class changePasswordRequestDto {
	
	@NotBlank(message = "Current password is required")
    private String currentPassword;
    
    @NotBlank(message = "New password is required")
    private String newPassword;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}
