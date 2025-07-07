package com.example.Ecommerce.Mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.Ecommerce.Repository.CartRepository;
import com.example.Ecommerce.Repository.ProductRepository;
import com.example.Ecommerce.Request.CartItemDto;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.CartItem;
import com.example.Ecommerce.model.Product;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

	@Mapping(source = "product.id", target = "productId")
    @Mapping(source = "cart.id", target = "cartId")
    CartItemDto toDto(CartItem cartItem);
	
	 @Mapping(target = "product", ignore = true)
	    @Mapping(target = "cart", ignore = true)
	    CartItem toEntity(CartItemDto cartItemDto);
	 
	 @Mapping(target = "product", ignore = true)
	    @Mapping(target = "cart", ignore = true)
	    @Mapping(target = "id", ignore = true)
	    void updateEntityFromDto(CartItemDto cartItemDto, @MappingTarget CartItem cartItem);
}
