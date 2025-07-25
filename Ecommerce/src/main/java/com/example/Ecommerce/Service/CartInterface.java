package com.example.Ecommerce.Service;

import java.math.BigDecimal;

import com.example.Ecommerce.Request.CartDto;

public interface CartInterface {
	
	CartDto getCart(Long userid);
	CartDto clearCart(Long userid);
	BigDecimal getTotalPrice(Long userid);
	
	

}
