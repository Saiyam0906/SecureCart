package com.example.SecureCart.Interface;

import java.util.List;

import com.example.SecureCart.Request.CategoryDto;
import com.example.SecureCart.Request.CategoryUpdateDto;
import com.example.SecureCart.model.Category;

public interface CategoryServiceInterface {
	CategoryDto getCategoryByid(Long id);
	CategoryDto getCategoryByName(String name);
	List<CategoryDto> getAllCategory();
	CategoryDto addCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryUpdateDto categoryupdateDto,Long id);
	void deleteCategoryByid(Long id);
	

}
