package com.example.Ecommerce.Mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.Ecommerce.Request.ProductDto;
import com.example.Ecommerce.Request.ProductUpdateDto;
import com.example.Ecommerce.model.Product;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {
    
	ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);
}