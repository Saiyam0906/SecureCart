package com.example.Ecommerce.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {	
	ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);// use to convert list of product to Dto 
    

}
