package com.example.SecureCart.Response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordResponseDto {
	
	 private String message;
	    private LocalDateTime changedAt;
	

}
