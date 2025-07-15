package com.example.Ecommerce.Request;

import com.example.Ecommerce.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
	
	    @NotBlank(message = "First name is required")
	    @Size(max = 50)
	    private String firstName;

	    @NotBlank(message = "Last name is required")
	    @Size(max = 50)
	    private String lastName;

	    @Email(message = "Email should be valid")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @NotBlank(message = "Password is required")
	    @Size(min = 3, message = "Password must be at least 3 characters")
	    private String password;

	    private Role role;

	    private boolean enabled = true;

	    private boolean accountNonLocked = true;

}
