package com.example.Ecommerce.Exception;

public class CartItemNotFoundException extends RuntimeException{
	public  CartItemNotFoundException(String message) {
		super(message);
	}

}
