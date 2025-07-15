package com.example.Ecommerce.Service.Cart;

import java.math.BigDecimal;

import com.example.Ecommerce.Request.CartDto;
import com.example.Ecommerce.model.Cart;

public interface CartInterface {
	
	CartDto getCart(Long userid);
	CartDto clearCart(Long userid);
	BigDecimal getTotalPrice(Long userid);
	
	

}
