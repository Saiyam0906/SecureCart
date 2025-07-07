package com.example.Ecommerce.Service;

import java.math.BigDecimal;

import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.model.Cart;

public interface CartInterface {
	
	CartDto getCart(Long id);
	void clearCart(Long id);
	BigDecimal getTotalPrice(Long id);
	
	

}
