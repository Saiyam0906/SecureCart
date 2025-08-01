package com.example.SecureCart.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.SecureCart.Request.CartDto;
import com.example.SecureCart.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
	
	
	@Mapping(target = "totalItems", expression = "java(cart.getTotalItems())")
    @Mapping(target = "empty", expression = "java(cart.isEmpty())")
	CartDto toDto(Cart cart);
	
	Cart toEntity(CartDto cartDto);

}
