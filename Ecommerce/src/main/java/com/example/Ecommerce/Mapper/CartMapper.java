package com.example.Ecommerce.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
	
	
	@Mapping(target = "totalItems", expression = "java(cart.getTotalItems())")
    @Mapping(target = "empty", expression = "java(cart.isEmpty())")
	CartDto toDto(Cart cart);
	
	Cart toEntity(CartDto cartDto);

}
