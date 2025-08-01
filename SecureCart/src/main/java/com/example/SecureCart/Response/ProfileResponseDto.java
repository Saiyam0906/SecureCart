package com.example.SecureCart.Response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
	    private Long id;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private boolean emailVerified;
	    private String phoneNumber;
	    private String profilePictureUrl;
	    private LocalDateTime createdAt;
	    private LocalDateTime lastLogin;
	    private AddressResponseDto defaultAddress;
	    
}
