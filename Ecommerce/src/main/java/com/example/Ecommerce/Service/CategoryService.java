package com.example.Ecommerce.Service;


import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.Ecommerce.Exception.CategoryAlredyExistException;
import com.example.Ecommerce.Exception.CategoryNotFoundException;
import com.example.Ecommerce.Repository.CategoryRepository;
import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.model.Category;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface{
	
	private final CategoryRepository categoryRepository;

	@Override
	public Category getCategoryByid(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(()->new CategoryNotFoundException("Category Dosent Exist with id"+id));
	
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name);
				
	}

	@Override
	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}



	@Override
	public void deleteCategoryByid(Long id) {
		categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
			throw new CategoryNotFoundException("Category Not found");
		});
		
		
	}

	@Override
	public Category addCategory(CategoryDto categoryDto) {
		 if (categoryRepository.existsByName(categoryDto.getName())) {
		        throw new CategoryAlredyExistException("Category already exists");
		    }
		    Category category = new Category(categoryDto.getName());
		    return categoryRepository.save(category);
	}		    

	@Override
	public Category updateCategory(CategoryDto categoryDto, Long id) {
		Category existingCategory = getCategoryByid(id); // throws if not found
	    existingCategory.setName(categoryDto.getName());
	    return categoryRepository.save(existingCategory);
	}

}
