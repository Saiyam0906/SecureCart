package com.example.Ecommerce.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Ecommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByCategoryName(String category);

	List<Product> findByBrandName(String brand);

	List<Product> findByCategoryAndBrand(String category, String brand);

	List<Product> findByName(String name);

	List<Product> findByBrandAndName(String brand, String name);

	Long countByBrandAndName(String brand, String name);

}
