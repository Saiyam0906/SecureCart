package com.example.Ecommerce.Exception;

public class OrderNotFound extends RuntimeException{
	public OrderNotFound(String message) {
		super(message);
	}

}
