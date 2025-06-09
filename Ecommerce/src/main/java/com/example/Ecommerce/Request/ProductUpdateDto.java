package com.example.Ecommerce.Request;


import java.util.Locale.Category;

import lombok.Data;

@Data
public class ProductUpdateDto {
	
	private String name;
    private String brand;
    private String description;
    private float price;
    private int inventory;
    private Category category;

	

}
