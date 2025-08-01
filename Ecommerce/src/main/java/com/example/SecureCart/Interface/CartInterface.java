package com.example.SecureCart.Interface;

import java.math.BigDecimal;

import com.example.SecureCart.Request.CartDto;

public interface CartInterface {
	
	CartDto getCart(Long userid);
	CartDto clearCart(Long userid);
	BigDecimal getTotalPrice(Long userid);
	
	

}
