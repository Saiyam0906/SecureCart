package com.example.Ecommerce.Service;

import java.util.List;

import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.model.Category;

public interface CategoryServiceInterface {
	Category getCategoryByid(Long id);
	Category getCategoryByName(String name);
	List<Category> getAllCategory();
	Category addCategory(CategoryDto categoryDto);
	Category updateCategory(CategoryDto categoryDto,Long id);
	void deleteCategoryByid(Long id);
	

}
