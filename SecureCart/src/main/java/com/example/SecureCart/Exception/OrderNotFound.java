package com.example.SecureCart.Exception;

public class OrderNotFound extends RuntimeException{
	public OrderNotFound(String message) {
		super(message);
	}

}
