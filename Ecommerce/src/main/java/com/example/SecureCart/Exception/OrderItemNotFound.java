package com.example.SecureCart.Exception;

public class OrderItemNotFound extends RuntimeException{
	
	public OrderItemNotFound(String message) {
		super(message);
	}

}
