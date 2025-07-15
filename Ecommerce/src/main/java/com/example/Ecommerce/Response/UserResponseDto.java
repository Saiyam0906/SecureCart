package com.example.Ecommerce.Response;

import java.time.LocalDateTime;

import com.example.Ecommerce.enums.Role;

import lombok.Data;

@Data
public class UserResponseDto {
    
	private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private boolean enabled;

    private boolean accountNonLocked;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
