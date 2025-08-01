package com.example.SecureCart.Mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.SecureCart.Repository.CartRepository;
import com.example.SecureCart.Repository.ProductRepository;
import com.example.SecureCart.Request.CartItemDto;
import com.example.SecureCart.model.Cart;
import com.example.SecureCart.model.CartItem;
import com.example.SecureCart.model.Product;

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
