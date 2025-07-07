package com.example.Ecommerce.Request;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductUpdateDto {
	
	private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private CategoryDto category;

	

}
