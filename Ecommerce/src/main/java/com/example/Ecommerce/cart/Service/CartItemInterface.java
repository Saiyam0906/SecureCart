package com.example.Ecommerce.cart.Service;

import com.example.Ecommerce.Request.CartItemDto;
import com.example.Ecommerce.model.CartItem;

public interface CartItemInterface {
	
	CartItemDto additem(CartItemDto cartItemDto);
	void removeitem(Long cartItemId);
	CartItemDto updateItem(Long cartItemId, CartItemDto cartItemDto);
	

}
