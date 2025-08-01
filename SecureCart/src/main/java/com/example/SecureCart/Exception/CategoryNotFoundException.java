package com.example.SecureCart.Exception;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException(String message) {
		super(message);
	}

}
