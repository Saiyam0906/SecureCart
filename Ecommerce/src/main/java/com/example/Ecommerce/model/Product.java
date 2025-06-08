package com.example.Ecommerce.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
	

	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String name;
      private String brand;
      private String description;
      private float price;
      private int inventory;
      
      @ManyToOne
      @JoinColumn(name="category_id")
      private Category category;
      
      @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
      private List<Image> images;

	public Product(String name, String brand, String description, float price, int inventory, Category category) {
		super();
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.price = price;
		this.inventory = inventory;
		this.category = category;
	}
      
      
      
      
	
}
