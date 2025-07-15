package com.example.Ecommerce.cart.Service;

import java.math.BigDecimal;

import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.model.Cart;

public interface CartInterface {
	
	CartDto getCart(Long userid);
	CartDto clearCart(Long userid);
	BigDecimal getTotalPrice(Long userid);
	
	

}
