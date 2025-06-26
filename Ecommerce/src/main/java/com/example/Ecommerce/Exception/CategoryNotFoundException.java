package com.example.Ecommerce.Exception;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException(String message) {
		super(message);
	}

}
