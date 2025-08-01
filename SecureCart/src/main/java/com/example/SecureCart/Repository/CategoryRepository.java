package com.example.SecureCart.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureCart.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	Category findByName(String name);

	boolean existsByName(String name);

}
