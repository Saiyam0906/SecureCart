package com.example.Ecommerce.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDto {
	private String firstName;
    private String lastName;
    private String phoneNumber;
   
}
