package com.example.Ecommerce.Request;


import lombok.Data;

@Data
public class ProductUpdateDto {
	
	private String name;
    private String brand;
    private String description;
    private float price;
    private int inventory;
    private CategoryDto category;

	

}
