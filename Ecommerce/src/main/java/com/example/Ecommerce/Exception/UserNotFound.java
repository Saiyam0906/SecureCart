package com.example.Ecommerce.Exception;

public class UserNotFound extends RuntimeException{
	public UserNotFound(String message) {
		super(message);
	}

}
