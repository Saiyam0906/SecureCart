package com.example.SecureCart.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.SecureCart.Request.CategoryDto;
import com.example.SecureCart.Request.CategoryUpdateDto;
import com.example.SecureCart.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
    CategoryDto toDto(Category category);  
    Category toEntity(CategoryDto categoryDto);
    List<CategoryDto> toDtoList(List<Category> categories);
}
