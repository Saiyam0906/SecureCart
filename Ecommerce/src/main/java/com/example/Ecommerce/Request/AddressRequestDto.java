package com.example.Ecommerce.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequestDto {
	
   
    @NotBlank(message = "Street is required")
    @Size(min = 3, max = 100, message = "Street must be between 3 and 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "\\d{5,6}", message = "Zip code must be 5 or 6 digits")
    private String zip;

    private Long userId;

}
