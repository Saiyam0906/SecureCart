package com.example.Ecommerce.Exception;

public class OrderItemNotFound extends RuntimeException{
	
	public OrderItemNotFound(String message) {
		super(message);
	}

}
