package com.example.Ecommerce.Response;

import lombok.Data;

@Data
public class AddressResponseDto {
	private Long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String userName;
    private boolean isDefault;
}
