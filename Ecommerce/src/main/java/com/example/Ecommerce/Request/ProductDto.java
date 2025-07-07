package com.example.Ecommerce.Request;

import java.math.BigDecimal;

import com.example.Ecommerce.model.Category;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class ProductDto {
	
	 private String name;
     private String brand;
     private String description;
     private BigDecimal price;
     private int inventory;
     private CategoryDto category;
     

}
