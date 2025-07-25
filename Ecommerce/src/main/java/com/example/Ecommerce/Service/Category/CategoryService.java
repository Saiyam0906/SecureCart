package com.example.Ecommerce.Service.Category;


import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.Ecommerce.Exception.CategoryAlredyExistException;
import com.example.Ecommerce.Exception.CategoryNotFoundException;
import com.example.Ecommerce.Mapper.CategoryMapper;
import com.example.Ecommerce.Repository.CategoryRepository;
import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.Request.CategoryUpdateDto;
import com.example.Ecommerce.model.Category;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface{
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryMapper categorymapper;

	@Override
	public CategoryDto getCategoryByid(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(()->new CategoryNotFoundException("Category Dosent Exist with id"+id));
		return categorymapper.toDto(category);
	}

	@Override
	public CategoryDto getCategoryByName(String name) {
		 Category category = Optional.ofNullable(categoryRepository.findByName(name))
			        .orElseThrow(() -> new CategoryNotFoundException("Category not found with name " + name));
			    return categorymapper.toDto(category);
	}
	
	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		return categorymapper.toDtoList(categories);
	}
	
	@Override
	public void deleteCategoryByid(Long id) {
		categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
			throw new CategoryNotFoundException("Category Not found");
		});
	}
	
	@Override
	public CategoryDto addCategory(CategoryDto categoryDto) {
		if (categoryRepository.existsByName(categoryDto.getName())) {
			throw new CategoryAlredyExistException("Category already exists");
		}
		Category category = categorymapper.toEntity(categoryDto);
		Category savedCategory = categoryRepository.save(category);
		return categorymapper.toDto(savedCategory);
	}
	
	@Override
	public CategoryDto updateCategory(CategoryUpdateDto categoryupdateDto, Long id) {
		Category existingCategory = categoryRepository.findById(id)
				.orElseThrow(()->new CategoryNotFoundException("Category Dosent Exist with id"+id));
		
		 existingCategory.setName(categoryupdateDto.getName());
		Category updatedCategory = categoryRepository.save(existingCategory);
		return categorymapper.toDto(updatedCategory);
	}

}
