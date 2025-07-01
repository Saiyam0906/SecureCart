package com.example.Ecommerce.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.Ecommerce.Request.CategoryDto;
import com.example.Ecommerce.Request.CategoryUpdateDto;
import com.example.Ecommerce.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
    CategoryDto toDto(Category category);  
    Category toEntity(CategoryDto categoryDto);
    List<CategoryDto> toDtoList(List<Category> categories);
}
