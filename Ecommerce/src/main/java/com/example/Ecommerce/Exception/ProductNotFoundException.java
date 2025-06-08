package com.example.Ecommerce.Exception;

public class ProductNotFoundException extends RuntimeException{
	
	public ProductNotFoundException(String message) {
		super(message);
	}

}
