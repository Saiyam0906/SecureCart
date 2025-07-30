package com.example.Ecommerce.Interface;

import java.util.List;

import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.Request.CategoryUpdateDto;
import com.example.Ecommerce.model.Category;

public interface CategoryServiceInterface {
	CategoryDto getCategoryByid(Long id);
	CategoryDto getCategoryByName(String name);
	List<CategoryDto> getAllCategory();
	CategoryDto addCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryUpdateDto categoryupdateDto,Long id);
	void deleteCategoryByid(Long id);
	

}
