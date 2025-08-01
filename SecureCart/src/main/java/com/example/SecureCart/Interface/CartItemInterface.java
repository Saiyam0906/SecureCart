package com.example.SecureCart.Interface;

import com.example.SecureCart.Request.CartItemDto;
import com.example.SecureCart.model.CartItem;

public interface CartItemInterface {
	
	CartItemDto additem(CartItemDto cartItemDto);
	void removeitem(Long cartItemId);
	CartItemDto updateItem(Long cartItemId, CartItemDto cartItemDto);
	

}
